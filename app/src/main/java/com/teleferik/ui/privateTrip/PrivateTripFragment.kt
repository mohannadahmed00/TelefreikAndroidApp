package com.teleferik.ui.privateTrip

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentHomeBinding
import com.teleferik.databinding.FragmentPrivateTripBinding
import com.teleferik.ui.home.HomeFragment
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.home.adapters.ViewPagerAdapter
import com.teleferik.ui.privateTrip.adapters.StationsAdapter
import com.teleferik.ui.privateTrip.adapters.TabsAdapter
import com.teleferik.utils.Constants
import com.teleferik.utils.showTopToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.exp

class PrivateTripFragment :
    BaseFragment<PrivateTripViewModel, FragmentPrivateTripBinding, PrivateRepo>() {
    private var adultCount: String = "1"
    private var currentStartDate: Long = 0
    private var currentEndDate: Long = 0
    private var selectedHour: Int = -1
    private var selectedMin: Int = -1
    private var selectedAmPm: String? = null


    override fun getViewModel(): Class<PrivateTripViewModel> {
        return PrivateTripViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentPrivateTripBinding = FragmentPrivateTripBinding.inflate(layoutInflater)

    override fun getFragmentRepo(): PrivateRepo {
        return PrivateRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun handleView() {
        //binding.includeStations.edtStart.requestFocus()
        initClicks()
        initTabs()
        preparePassengersSpinner()
        handleTripDates()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initClicks() {
        binding.includeTopBar.imgBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(
                PrivateTripFragmentDirections.actionPrivateFragmentTripToPrivateConfirmationFragment()
            )
        }
        binding.includeStations.edtStart.setOnTouchListener{_,event ->
            if (MotionEvent.ACTION_UP == event.action)
            handleStartStation()
            binding.includeStations.edtStart.performClick()
            true
        }
        binding.includeStations.edtEnd.setOnTouchListener{_,event ->
            if (MotionEvent.ACTION_UP == event.action)
                handleEndStation()
            binding.includeStations.edtEnd.performClick()
            true
        }
        binding.includeDates.tvStartTime.setOnClickListener { handleTripTimes(binding.includeDates.tvStartTime) }
        binding.includeDates.tvEndTime.setOnClickListener { handleTripTimes(binding.includeDates.tvEndTime) }

    }

    private fun handleStartStation() {
        val builder = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.view_private_search, null)
        val exLVStation = view.findViewById<ExpandableListView>(R.id.exLvStations)
        val expandableListDetail = ExpandableListDataPump.getData()
        val expandableListTitle = expandableListDetail.keys.toList()
        val adapter = StationsAdapter(context,expandableListTitle,expandableListDetail)
        exLVStation.setAdapter(adapter)
        exLVStation.setOnGroupExpandListener {
            Toast.makeText(context, expandableListTitle[it] + "List Expanded", Toast.LENGTH_SHORT).show()
        }
        exLVStation.setOnGroupCollapseListener {
            Toast.makeText(context, expandableListTitle[it] + "List Collapsed", Toast.LENGTH_SHORT).show()
        }
        exLVStation.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            Toast.makeText(context, expandableListTitle[groupPosition] + " -> " + expandableListDetail[expandableListTitle[groupPosition]]?.get(childPosition), Toast.LENGTH_SHORT).show()
            false
        }
        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        builder.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.trans)))
        builder.show()

    }
    private fun handleEndStation() {
        val builder = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.view_private_search, null)
        val exLVStation = view.findViewById<ExpandableListView>(R.id.exLvStations)
        val expandableListDetail = ExpandableListDataPump.getData()
        val expandableListTitle = expandableListDetail.keys.toList()
        val adapter = StationsAdapter(context,expandableListTitle,expandableListDetail)
        exLVStation.setAdapter(adapter)
        exLVStation.setOnGroupExpandListener {
            Toast.makeText(context, expandableListTitle[it] + "List Expanded", Toast.LENGTH_SHORT).show()
        }
        exLVStation.setOnGroupCollapseListener {
            Toast.makeText(context, expandableListTitle[it] + "List Collapsed", Toast.LENGTH_SHORT).show()
        }
        exLVStation.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            Toast.makeText(context, expandableListTitle[groupPosition] + " -> " + expandableListDetail[expandableListTitle[groupPosition]]?.get(childPosition), Toast.LENGTH_SHORT).show()
            false
        }
        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        builder.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.trans)))
        builder.show()

    }

    private fun initTabs() {
        binding.includeTopBar.topTitle.text = getString(R.string.private_trip)
        binding.tlPrivateTrips.addTab(binding.tlPrivateTrips.newTab().setText(R.string.private_bus))
        binding.tlPrivateTrips.addTab(
            binding.tlPrivateTrips.newTab().setText(R.string.private_mini_bus)
        )
        binding.tlPrivateTrips.addTab(
            binding.tlPrivateTrips.newTab().setText(R.string.private_limousine)
        )
        binding.tlPrivateTrips.tabGravity = TabLayout.GRAVITY_FILL
        binding.tlPrivateTrips.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.includeInfo.tvTripType.text = tab!!.text
                when (tab.position) {
                    0 -> binding.includeInfo.ivIcon.setImageResource(R.drawable.ic_bus)
                    1 -> {
                        binding.includeInfo.ivIcon.setImageResource(R.drawable.ic_minibus)
                        if (adultCount.toInt() >18){
                            adultCount="18"
                            binding.includePassengersNumber.spinAdults.setSelection(17)
                        }
                    }
                    else -> {
                        binding.includeInfo.ivIcon.setImageResource(R.drawable.ic_car)
                        if (adultCount.toInt() >4){
                            adultCount="4"
                            binding.includePassengersNumber.spinAdults.setSelection(3)
                        }
                    }
                }
                //Toast.makeText(context,tab!!.text,Toast.LENGTH_LONG).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        DrawableCompat.setTint(
            DrawableCompat.wrap(binding.includeInfo.ivIcon.drawable),
            ContextCompat.getColor(binding.includeInfo.ivIcon.context, R.color.gray_3)
        )
    }

    private fun preparePassengersSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.private_passengers,
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
                    when {
                        pos + 1 in 19..28 -> binding.tlPrivateTrips.selectTab(binding.tlPrivateTrips.getTabAt(0))//must bus

                        pos + 1 in 5..18 -> {
                            if (binding.tlPrivateTrips.selectedTabPosition == 2){
                                binding.tlPrivateTrips.selectTab(binding.tlPrivateTrips.getTabAt(1))
                            }
                        }
                    }
                    adultCount = (pos + 1).toString()
                }

                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
    }

    private fun handleTripDates() {
        binding.includeDates.tvStartDate.setOnClickListener {
            currentStartDate = Date().time
            // date validator
            val dateValidator: CalendarConstraints.DateValidator =
                DateValidatorPointForward.from(currentStartDate)
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
                if (currentStartDate > currentEndDate) {
                    // reset end date
                    currentEndDate = 0
                    binding.includeDates.tvEndDate.text = ""
                    //binding.includeDates.tvEndDate.isEnabled = true
                }

            }
            datePicker.show(childFragmentManager, "")
        }
        binding.includeDates.tvEndDate.setOnClickListener {
            currentEndDate = Date().time
            // date validator
            val dateValidator: CalendarConstraints.DateValidator =
                DateValidatorPointForward.from(currentEndDate)
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
                if (it < currentStartDate) {
                    Toast.makeText(context, "Arrival > start", Toast.LENGTH_LONG).show()
                } else {
                    currentEndDate = it
                    val simpleDateFormat =
                        SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH)
                    val dateString: String = simpleDateFormat.format(it)
                    binding.includeDates.tvEndDate.text = dateString
                }
            }
            datePicker.show(childFragmentManager, "")
        }
        //set start & end date
        //val currentDate = SimpleDateFormat(Constants.DATE_YYYY_MM_DD_FORMAT, Locale.ENGLISH).format(Date())
        //binding.includeDates.tvStartDate.text = currentDate
        //binding.includeDates.tvEndDate.text = currentDate
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleTripTimes(tvDate: TextView) {
        val builder = AlertDialog.Builder(context).create()
        val view = layoutInflater.inflate(R.layout.view_clock, null)
        val btnOk = view.findViewById<Button>(R.id.btnOk)
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        builder.setView(view)
        if (selectedHour != -1) {
            if (selectedAmPm == "AM") {
                if (selectedHour == 12) timePicker.hour = selectedHour - 12 else timePicker.hour =
                    selectedHour
            } else {
                if (selectedHour == 12) timePicker.hour = selectedHour else timePicker.hour =
                    selectedHour + 12
            }

        }
        if (selectedMin != -1) { timePicker.minute = selectedMin }
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            selectedHour = hour
            selectedMin = minute
            selectedAmPm = ""
            // AM_PM decider logic
            when {
                selectedHour == 0 -> {
                    selectedHour += 12
                    selectedAmPm = "AM"
                }
                selectedHour == 12 -> {
                    selectedAmPm = "PM"
                }
                selectedHour > 12 -> {
                    selectedHour -= 12
                    selectedAmPm = "PM"
                }
                else -> selectedAmPm = "AM"
            }
            val hourStr = if (selectedHour < 10) "0$selectedHour" else selectedHour
            val minStr = if (selectedMin < 10) "0$selectedMin" else selectedMin
            // display format of time
            val msg = "$hourStr : $minStr $selectedAmPm"
            tvDate.text = msg
        }
        btnOk.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        //dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.trans)))
        builder.show()
    }


}