package com.teleferik.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.hmlabs.kheirzaman.utils.localizationUtils.LocaleManager
import com.teleferik.AppController
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.Transport
import com.teleferik.data.ViewModelFactory
import com.teleferik.data.network.Resource
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentHomeBinding
import com.teleferik.models.ErrorResponse
import com.teleferik.models.promotionalOffer.Offer
import com.teleferik.models.skyscanner.airPorts.Place
import com.teleferik.ui.home.adapters.TransportationTypeAdapter
import com.teleferik.ui.home.adapters.ViewPagerAdapter
import com.teleferik.ui.home.adapters.ViewPagerPageChangeListener
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, HomeRepo>(),
    TransportationTypeAdapter.OnItemClickListener {
    var mStartDestinationPlace: Place? = null
    var mEndDestinationPlace: Place? = null

    var mStartDestinationLocation: LocationsResponseItem? = null
    var mEndDestinationLocation: LocationsResponseItem? = null

    lateinit var mProfileViewModel: ProfileViewModel
    var currentStartDate by Delegates.notNull<Long>()
    var currentEndDate by Delegates.notNull<Long>()
    var adultCount: String = "1"
    var childCount: String = "0"
    lateinit var selectedClass: String
    //var hasClass: Boolean = false
    var displayChildCount: Boolean = false
    private var runnable: Runnable? = null
    var category: String = ""


    object Classes {
        const val Sky_Scanner_CLASS_ECONOMY = "CABIN_CLASS_ECONOMY"
        const val Sky_Scanner_CLASS_BUSINESS = "CABIN_CLASS_BUSINESS"
        const val Sky_Scanner_CLASS_BUSINESS_MEN = "CABIN_CLASS_FIRST"
        const val Sky_Scanner_CLASS_PREMIUM = "CABIN_CLASS_PREMIUM_ECONOMY"
    }

    companion object {
        var isNotificationsHasUnread = false
    }

    private val mTransportationTypeAdapter: TransportationTypeAdapter by lazy {
        TransportationTypeAdapter(
            mutableListOf(
                Transport("Flight", getString(R.string.plan), R.drawable.ic_plane),
                Transport("Voyage", getString(R.string.voyage), R.drawable.ic_ship),
                Transport("Trains", getString(R.string.train), R.drawable.ic_high_speed_train),
                Transport("Bus", getString(R.string.bus), R.drawable.ic_bus),
                Transport("Private", getString(R.string.private_trip), R.drawable.ic_premium),
            ), this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.setDecorFitsSystemWindows(false)
        } else {
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        //val lang = if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC) "ar-AE" else "en-UK"
        setFragmentResultListener(Constants.START_DESTINATION) { _, bundle ->
            if (category == "Flight") {
                val data = bundle.getParcelable<Place>(Constants.START_DESTINATION)
                Log.d("Adel Fl St Dest : ",data.toString())
                if (data != null) {
                    binding.include.edtStart.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                    binding.include.edtStart.setText(data.placeName)
                    mStartDestinationPlace = data
                }
            }
            else {
                val data = bundle.get(Constants.START_DESTINATION) as MutableMap<*, *>
                Log.d("Adel other St Dest : ",data["item"].toString())
                if (data.isNotEmpty()) {
                    mStartDestinationLocation = data["item"] as LocationsResponseItem
                    binding.include.edtStart.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                    binding.include.edtStart.setText(mStartDestinationLocation?.name)
                }
            }
        }
        setFragmentResultListener(Constants.ARRIVAL_DESTINATION) { _, bundle ->
            if (category == "Flight") {
                val data = bundle.getParcelable<Place>(Constants.ARRIVAL_DESTINATION)
                if (data != null) {
                    binding.include.edtEnd.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                    binding.include.edtEnd.setText(data.placeName)
                    mEndDestinationPlace = data
                }
            } else {
                val data = bundle.get(Constants.ARRIVAL_DESTINATION) as MutableMap<*, *>
                if (data.isNotEmpty()) {
                    mEndDestinationLocation = data["item"] as LocationsResponseItem
                    binding.include.edtEnd.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                    binding.include.edtEnd.setText(mEndDestinationLocation?.name)
                }
            }

        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentHomeBinding.inflate(layoutInflater)

    override fun handleView() {
        selectedClass = Classes.Sky_Scanner_CLASS_ECONOMY
        binding.include.edtStart.requestFocus()
        binding.includeDates.tvReturnDate.isEnabled = false
        binding.includeClasses.root.visibility = View.GONE
        binding.includePassengersNumber.spinChild.isEnabled = false
        prepareAdultsSpinner()
        initProfileViewModel()
        initTransportationRecyclerView()
        initClicks()
        handleTripDates()
        setStartAndEndDate()
        makeTripTypeSelected(
            binding.includeTripType.btnGoAndBack,
            binding.includeTripType.btnGo
        )
        checkAllNotificationRead()
        handleNotificationCases()
        promotionalOffersList()
    }

    private fun handleNotificationCases() {
        val type = requireActivity().intent.getIntExtra(Constants.NOTIFICATION_TYPE, 0)
        val url = requireActivity().intent.getStringExtra(Constants.NOTIFICATION_URL)
        if (!url.isNullOrEmpty()) {
            requireActivity().intent.removeExtra(Constants.NOTIFICATION_URL)
            requireActivity().openWebView(url)
        } else if (type == Constants.NOTIFICATIONS_TYPE.NOTIFICATION_CENTER)
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNotificationFragment())
        else if (type == Constants.NOTIFICATIONS_TYPE.SUPPORT_TICKET)
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSupportGraph())
        requireActivity().intent.removeExtra(Constants.NOTIFICATION_TYPE)
    }

    private fun checkAllNotificationRead() {
        mProfileViewModel.notificationsList()
        observeNotificationList()
    }

    private fun promotionalOffersList() {
        mViewModel.promotionalOffersList()
        observePromotionalOffersList()
    }

    private fun observeNotificationList() {
        mProfileViewModel.notificationListResponse.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Success -> {
                    if (it.value.data!!.isNotEmpty()) {
                        isNotificationsHasUnread =
                            it.value.data.any { it.read == Constants.NOT_READ }
                        if (it.value.data.any { it.read == Constants.NOT_READ })
                            binding.imgNotifications.setImageResource(R.drawable.ic_notification)
                        else
                            binding.imgNotifications.setImageResource(R.drawable.ic_notifications_black_24dp)
                    }
                }
                is Resource.Failure -> {
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun observePromotionalOffersList() {
        mViewModel.promotionalOffersList.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Success -> {
                    if (it.value.data!!.offers.isNotEmpty()) {
                        setupPromotionalOffers(it.value.data.offers)
                    } else {
                        showTopToast("empty")
                    }
                }
                is Resource.Failure -> {
                    showTopToast("Failed")
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    private fun initProfileViewModel() {
        val factory =
            ViewModelFactory(ProfileRepo(remoteDataSource.buildApi(ApisService::class.java)))
        mProfileViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    /*private fun setStartAndEndDate() {
        val currentDate =
            SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH).format(Date())
        binding.includeDates.tvStartDate.text = currentDate
        //binding.includeDates.tvReturnDate.text = currentDate
    }
    private fun handleTripDates() {

        binding.includeDates.tvStartDate.setOnClickListener {
            currentStartDate = Date().time
            currentEndDate = Date().time

            //currentStartDate = Date().time
            // date validator
            val dateValidator: DateValidator = DateValidatorPointForward.from(currentStartDate)
            // set start of calender + validator
            val constraintsBuilder = CalendarConstraints.Builder().setStart(currentStartDate)
            constraintsBuilder.setValidator(dateValidator)
            // initiate calender
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.travel_date))
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                currentStartDate = it
                val simpleDateFormat =
                    SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH)
                val dateString: String = simpleDateFormat.format(it)
                binding.includeDates.tvStartDate.text = dateString
                binding.includeDates.tvReturnDate.text = "" // reset date
                binding.includeDates.tvReturnDate.isEnabled = true
            }
            datePicker.show(childFragmentManager, "")
        }


        binding.includeDates.tvReturnDate.setOnClickListener {
            //currentEndDate = Date().time
            // date validator
            val dateValidator: DateValidator = DateValidatorPointForward.from(currentEndDate)
            // set start of calender + validator
            val constraintsBuilder = CalendarConstraints.Builder().setStart(currentEndDate)
            constraintsBuilder.setValidator(dateValidator)
            // initiate calender
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.back_day))
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                currentEndDate = it
                val simpleDateFormat =
                    SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH)
                val dateString: String = simpleDateFormat.format(it)
                binding.includeDates.tvReturnDate.text = dateString
            }
            datePicker.show(childFragmentManager, "")
        }

    }*/
    private fun setStartAndEndDate() {
        val currentDate =  SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH).format(Date())
        binding.includeDates.tvStartDate.text = currentDate
        //binding.includeDates.tvReturnDate.text = currentDate
    }

    private fun handleTripDates() {
        currentStartDate = Date().time
        binding.includeDates.tvStartDate.setOnClickListener {
            // date validator
            val dateValidator: DateValidator = DateValidatorPointForward.now()
            // set start of calender + validator
            val constraintsBuilder = CalendarConstraints.Builder().setStart(Date().time)
            constraintsBuilder.setValidator(dateValidator)
            // initiate calender
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(currentStartDate)
                    .setTitleText(getString(R.string.travel_date))
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                currentStartDate = it
                val simpleDateFormat =
                    SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH)
                val dateString: String = simpleDateFormat.format(it)
                binding.includeDates.tvStartDate.text = dateString
                binding.includeDates.tvReturnDate.text = "" // reset date
                binding.includeDates.tvReturnDate.isEnabled = true
            }
            datePicker.show(childFragmentManager, "")
        }
        currentEndDate = Date().time
        binding.includeDates.tvReturnDate.setOnClickListener {


            // date validator
            val dateValidator: DateValidator = DateValidatorPointForward.from(currentStartDate)
            // set start of calender + validator
            val constraintsBuilder = CalendarConstraints.Builder().setStart(currentStartDate)
            constraintsBuilder.setValidator(dateValidator)
            // initiate calender
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(currentEndDate)
                    .setTitleText(getString(R.string.back_day))
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            datePicker.addOnPositiveButtonClickListener {
                val simpleDateFormat =
                    SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH)
                val dateString: String = simpleDateFormat.format(it)
                binding.includeDates.tvReturnDate.text = dateString
            }
            datePicker.show(childFragmentManager, "")
        }
    }

    private fun initClicks() {
        binding.imgNotifications.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToNotificationFragment()
            )
        }

        binding.btnSearch.setOnClickListener {
            if (isSearchFormValid()) {
<<<<<<< HEAD
                if (category == "Flight") {
                    callSearch()
                }else if (category =="Bus"){

                } else findNavController().navigate(
                    //HomeFragmentDirections.actionNavigationHomeToSeatSelectionFragment()
                HomeFragmentDirections.actionNavigationHomeToSearchResultsFragment(category,null)
                )
=======
                when (category) {
                    "Flight" -> {
                        callSearch()
                    }
                    "Bus" -> {

                        val simpleDateFormat = SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH)
                        val dateString: String = simpleDateFormat.format(currentStartDate)
                        Log.e("BusSearchTripsResponse1","$category--$mStartDestinationLocation--$mEndDestinationLocation--$dateString")
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToSearchResultsFragment(
                                category,
                                null,
                                mStartDestinationLocation?.id.toString(),
                                mEndDestinationLocation?.id.toString(),
                                mStartDestinationLocation?.name.toString(),
                                mEndDestinationLocation?.name.toString(),
                                dateString
                            )
                        )
                    }
                    else -> {
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToSearchResultsFragment(
                                category,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                    }
                }
>>>>>>> tmp
            }
        }

        binding.includeTripType.btnGo.setOnClickListener {
            makeTripTypeSelected(
                binding.includeTripType.btnGo,
                binding.includeTripType.btnGoAndBack
            )
            mViewModel.setTripType(0)
            binding.includeDates.datesguideline.setGuidelinePercent(1F)
            binding.includeDates.tvReturnDate.showHideView(mViewModel.getTripType() != 0)
            binding.includeDates.tvReturnTitle.showHideView(mViewModel.getTripType() != 0)
        }

        binding.includeTripType.btnGoAndBack.setOnClickListener {
            if (category == "Bus"){
                Toast.makeText(context,"This option cannot be used if you choose Bus",Toast.LENGTH_LONG).show()
            }else {
                makeTripTypeSelected(
                    binding.includeTripType.btnGoAndBack,
                    binding.includeTripType.btnGo
                )
                mViewModel.setTripType(1)
                binding.includeDates.datesguideline.setGuidelinePercent(0.5F)
                binding.includeDates.tvReturnDate.showHideView(mViewModel.getTripType() != 0)
                binding.includeDates.tvReturnTitle.showHideView(mViewModel.getTripType() != 0)
            }
        }

        binding.include.edtStart.setOnTouchListener { _, event ->
            if(checkSelectCategory()) {
                if (MotionEvent.ACTION_UP == event.action) {
                    if (category == "Flight") {
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToSearchAriPortsFragment()
                                .apply {
                                    searchKey =
                                        binding.include.edtStart.captureText().ifEmpty { "-" }
                                })
                    } else {
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToSearchLocationsFragment()//pass cities list
                                .apply {
                                    searchKey =
                                        binding.include.edtStart.captureText().ifEmpty { "-" }
                                }
                        )
                    }
                }
                binding.include.edtStart.performClick()
                 // return is important...
            }else{
                showTopToast(getString(R.string.select_category))
            }
            true

        }

        binding.include.edtEnd.setOnTouchListener { _, event ->
            if(checkSelectCategory()) {
                if (MotionEvent.ACTION_UP == event.action) {
                    if (category == "Flight") {
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToSearchAriPortsFragment()
                                .apply {
                                    isSearchFromStart = false
                                    searchKey = binding.include.edtEnd.captureText().ifEmpty { "-" }
                                })
                    } else {
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToSearchLocationsFragment()
                                .apply {
                                    isSearchFromStart = false
                                    searchKey = binding.include.edtEnd.captureText().ifEmpty { "-" }
                                }
                        )
                    }
                }
                binding.include.edtEnd.performClick()
            }else{
                showTopToast(getString(R.string.select_category))
            }
            true// return is important...
        }

        binding.include.switchDirections.setOnClickListener {
            val temp = binding.include.edtStart.text
            binding.include.edtStart.text = binding.include.edtEnd.text
            binding.include.edtEnd.text = temp
        }

        binding.includeClasses.btnEconomy.setOnClickListener {
            makeTripClassSelected(
                binding.includeClasses.btnEconomy,
                listOf(
                    binding.includeClasses.btnBusiness,
                    binding.includeClasses.btnBusinessMen,
                    binding.includeClasses.btnPremium
                )
            )
            selectedClass = Classes.Sky_Scanner_CLASS_ECONOMY
        }

        binding.includeClasses.btnBusiness.setOnClickListener {
            makeTripClassSelected(
                binding.includeClasses.btnBusiness,
                listOf(
                    binding.includeClasses.btnEconomy,
                    binding.includeClasses.btnBusinessMen,
                    binding.includeClasses.btnPremium
                )
            )
            selectedClass = Classes.Sky_Scanner_CLASS_BUSINESS
        }

        binding.includeClasses.btnBusinessMen.setOnClickListener {
            makeTripClassSelected(
                binding.includeClasses.btnBusinessMen,
                listOf(
                    binding.includeClasses.btnEconomy,
                    binding.includeClasses.btnBusiness,
                    binding.includeClasses.btnPremium
                )
            )
            selectedClass = Classes.Sky_Scanner_CLASS_BUSINESS_MEN
        }

        binding.includeClasses.btnPremium.setOnClickListener {
            makeTripClassSelected(
                binding.includeClasses.btnPremium,
                listOf(
                    binding.includeClasses.btnEconomy,
                    binding.includeClasses.btnBusiness,
                    binding.includeClasses.btnBusinessMen
                )
            )
            selectedClass = Classes.Sky_Scanner_CLASS_PREMIUM
        }
    }

    private fun isSearchFormValid(): Boolean {
        if (!checkSelectCategory()) {
            showTopToast(getString(R.string.select_category))
            return false
        }
        if (mStartDestinationPlace == null && mStartDestinationLocation == null) {
            showTopToast(getString(R.string.select_start_station))
            return false
        }
        if (mEndDestinationPlace == null && mEndDestinationLocation == null) {
            showTopToast(getString(R.string.select_arrival_station))
            return false
        }
        if (binding.includeDates.tvStartDate.text.isEmpty()) {
            showTopToast(getString(R.string.choose_travel_date))
            return false
        }
        if (mViewModel.getTripType() == 1) {
            if (binding.includeDates.tvReturnDate.text.isEmpty()) {
                showTopToast(getString(R.string.choose_back_date))
                return false
            }
        }
        if (!::selectedClass.isInitialized) {
            showTopToast(getString(R.string.choose_class))
            return false
        }
        return true
    }

    private fun checkSelectCategory(): Boolean {
        return mTransportationTypeAdapter.list.any { it.isSelected }
    }


    private fun makeTripTypeSelected(viewToSelect: TextView?, viewToUnSelect: TextView) {
        viewToSelect!!.setBackgroundResource(R.drawable.bg_white_selector)
        viewToSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.base_app_color))
        viewToUnSelect.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.base_app_color
            )
        )
        viewToUnSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
    }

    private fun makeTripClassSelected(viewToSelect: TextView?, viewToUnSelect: List<TextView>) {
        viewToSelect!!.setBackgroundResource(R.drawable.bg_white_selector)
        viewToSelect.setTextColor(ContextCompat.getColor(requireActivity(), R.color.base_app_color))
        viewToUnSelect.forEach {
            it.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.base_app_color
                )
            )
            it.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        }
    }

    override fun onCategoryClicked(item: Transport, pos: Int, list: MutableList<Transport>) {
        when (item.type) {
            "Trains" -> {
                Toast.makeText(context,getString(R.string.soon),Toast.LENGTH_LONG).show()
            }
            "Private" -> {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToPrivateGraph())
            }
            else -> {
                item.isSelected = !item.isSelected
                if (item.type != category) {
                    category = item.type
                    binding.include.edtStart.setText("")
                    binding.include.edtEnd.setText("")
                    mStartDestinationLocation = null
                    mStartDestinationPlace = null
                    mEndDestinationLocation = null
                    mEndDestinationPlace = null
                } else {
                    if (!item.isSelected) {
                        category = ""
                        binding.include.edtStart.setText("")
                        binding.include.edtEnd.setText("")
                        mStartDestinationLocation = null
                        mStartDestinationPlace = null
                        mEndDestinationLocation = null
                        mEndDestinationPlace = null
                    }
                }

                for (i in list.indices) {
                    if (i != pos) {
                        list[i].isSelected = false
                    }
                    if (list[i].type == "Flight") {
                        if (list[i].isSelected) {
                            binding.includeClasses.root.visibility = View.VISIBLE
                        } else {
                            binding.includeClasses.root.visibility = View.GONE
                        }
                    }else if (list[i].type == "Bus"){
                        if (list[i].isSelected) {
                            makeTripTypeSelected(
                                binding.includeTripType.btnGo,
                                binding.includeTripType.btnGoAndBack
                            )
                            mViewModel.setTripType(0)
                            binding.includeDates.datesguideline.setGuidelinePercent(1F)
                            binding.includeDates.tvReturnDate.showHideView(false)
                            binding.includeDates.tvReturnTitle.showHideView(false)
                        } else {
                            makeTripTypeSelected(
                                binding.includeTripType.btnGoAndBack,
                                binding.includeTripType.btnGo
                            )
                            mViewModel.setTripType(1)
                            binding.includeDates.datesguideline.setGuidelinePercent(0.5F)
                            binding.includeDates.tvReturnDate.showHideView(true)
                            binding.includeDates.tvReturnTitle.showHideView(true)
                        }
                    }

                }
                mTransportationTypeAdapter.notifyDataSetChanged()
            }
        }


        /*hasClass = when (list[0].type) {
            "Flight" -> {

                true
            }
            else -> {
                false
            }
        }*/

    }

    private fun initTransportationRecyclerView() {
        binding.rvTransportationType.adapter = mTransportationTypeAdapter
    }


    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    private fun callSearch() {
        loading.show()
        val returnDate =
            if (mViewModel.getTripType() == 1) binding.includeDates.tvReturnDate.text.toString() else ""
        val lang =
            if (AppController.localeManager?.language == LocaleManager.LANGUAGE_ARABIC) "ar-AE" else "en-UK"
        remoteDataSource.buildApi(ApisService::class.java).createSesstion(
            "https://partners.api.skyscanner.net/apiservices/pricing/v1.0",
            selectedClass,
            "EG",
            "EGP",
            lang,
            "iata",
            getIataCode(mStartDestinationPlace?.placeId!!),
            getIataCode(mEndDestinationPlace?.placeId!!),
            binding.includeDates.tvStartDate.text.toString(),
            returnDate,
            adultCount.toInt(),
            childCount.toInt(),
            0,
            "prtl6749387986743898559646983194"
        ).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                loading.cancel()
                handelPollSessionResponse(response)
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                loading.cancel()
            }

        })
    }


    private fun callBusTripSearch(){
        mViewModel.getBusTripsSearch(
            from = mStartDestinationLocation?.id.toString(),
            to = mEndDestinationLocation?.id.toString(),
            date = binding.includeDates.tvStartDate.text.toString()
        )

        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToSearchResultsFragment(category,"")
        )
    }
    private fun handelPollSessionResponse(response: Response<Any>) {
        if (response.code() == 400) {
            val errorBody = response.errorBody()?.string()
            val baseResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            showTopToast(baseResponse.message)
        } else {
            val headerList = response.headers()
            for (header in headerList) {
                Log.v("header", "${header.first} ------> ${header.second}")
                if (header.first == "Location") {
                    findNavController().navigate(
                        /*HomeFragmentDirections.actionNavigationHomeToSearchResultsFragment(
                            category,
                            header.second
                        )*/
                    HomeFragmentDirections.actionNavigationHomeToSearchResultsFragment(category,header.second,null,null,null,null,null)
                    )
                    return
                }
            }
        }

    }

    private fun getIataCode(placeId: String): String {

        return placeId.replace("-sky", "")
    }

    private fun prepareAdultsSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.passengers_adult,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.includePassengersNumber.spinAdults.adapter = adapter
        }

        binding.includePassengersNumber.spinAdults.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    adultCount = (pos + 1).toString()
                    binding.includePassengersNumber.spinChild.isEnabled = adultCount != "4"
                    prepareChildSpinner(4 - adultCount.toInt())
                    if (displayChildCount) {
                        showTopToast(
                            resources.getQuantityString(
                                R.plurals.childrens_allowed,
                                4 - adultCount.toInt(),
                                4 - adultCount.toInt()
                            )
                        )
                    }
                    displayChildCount = true
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
    }

    private fun prepareChildSpinner(available: Int) {
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.passengers_child).toMutableList()
        ) {
            override fun isEnabled(position: Int): Boolean {
                // disable the third item of spinner
                return position <= available
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.includePassengersNumber.spinChild.adapter = adapter
        binding.includePassengersNumber.spinChild.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    childCount = pos.toString()
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
    }

    private fun setupPromotionalOffers(newsData: List<Offer>) {
        if (newsData.isNotEmpty()) {
            val layouts = mutableListOf<Int>()
            for (n in newsData.indices step 1) {
                layouts.add(R.layout.slide_promotional_offer)
            }
            addViewPagerBottomDots(
                requireContext(),
                0,
                layouts.size,
                1,
                binding.includePromotionalOffers.dots
            )
            binding.includePromotionalOffers.promotionaloffers.adapter =
                ViewPagerAdapter(requireContext(), layouts, newsData)
            binding.includePromotionalOffers.promotionaloffers.addOnPageChangeListener(object :
                ViewPagerPageChangeListener(
                    requireActivity(),
                    layouts.size,
                    binding.includePromotionalOffers.dots
                ) {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }
            })
            startAutoSlider(newsData.count())
        }
    }

    private fun startAutoSlider(count: Int) {
        runnable = Runnable {
            var pos: Int = binding.includePromotionalOffers.promotionaloffers.currentItem
            pos += 1
            if (pos >= count) pos = 0
            binding.includePromotionalOffers.promotionaloffers.currentItem = pos
            binding.includePromotionalOffers.promotionaloffers.postDelayed(runnable, 3000)
        }
        binding.includePromotionalOffers.promotionaloffers.postDelayed(runnable, 3000)
    }

    override fun onDestroy() {
        if (runnable != null) binding.includePromotionalOffers.promotionaloffers.removeCallbacks(
            runnable
        )
        super.onDestroy()
    }
}