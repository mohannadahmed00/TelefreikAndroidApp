package com.teleferik.ui.seatSelection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.fragment.findNavController
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSeatSelectionBinding
import com.teleferik.models.seats.Seat
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.seatSelection.adapter.SeatSelectionAdapter
import com.teleferik.ui.seatSelection.compose.Status

@ExperimentalFoundationApi
class SeatSelectionFragment :
    BaseFragment<HomeViewModel, FragmentSeatSelectionBinding, HomeRepo>(),
    SeatSelectionAdapter.OnItemClickListener {
    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    private lateinit var seatSelectionAdapterLeft: SeatSelectionAdapter
    private lateinit var seatSelectionAdapterRight: SeatSelectionAdapter


    private val leftList =  listOf(1,2,5,6,9,10,13,14,17,18,21,22,25,26,29,30,33,34,37,38)
    val rightList = listOf(3,4,7,8,11,12,15,16,19,20,23,24,27,28,31,32,35,36,39,40)

    private val fakeReservedSeats = listOf(1,2,7,8,10)

    private fun getFakeData():Pair<MutableList<Seat>,MutableList<Seat>>{
        val leftSeats = mutableListOf<Seat>()
        val rightSeats = mutableListOf<Seat>()

        for (i in 1..40) {
            if (i in leftList) {
                if (i in fakeReservedSeats){
                    leftSeats.add(Seat(num = i, status = Status.Reserved))
                }else{
                    leftSeats.add(Seat(num = i))
                }
            } else {
                if (i in fakeReservedSeats){
                    rightSeats.add(Seat(num = i, status = Status.Reserved))
                }else{
                    rightSeats.add(Seat(num = i))
                }

            }
        }
        return Pair(leftSeats,rightSeats)
    }

    var selectedSeats = mutableListOf<Int>()



    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentSeatSelectionBinding.inflate(layoutInflater)

    override fun handleView() {
        initClicks()
    }
    private fun initClicks() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp() }
        binding.btnConfirm.setOnClickListener { findNavController().navigate(SeatSelectionFragmentDirections.actionSeatSelectionFragmentToSeatConfirmationFragment()) }
        seatSelectionAdapterLeft = SeatSelectionAdapter(getFakeData().first,this)
        binding.rvLiftSide.adapter = seatSelectionAdapterLeft
        seatSelectionAdapterRight = SeatSelectionAdapter(getFakeData().second,this)
        binding.rvRightSide.adapter = seatSelectionAdapterRight

        /*binding.composeSeatsView.apply { setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
                setContent { MaterialTheme { SeatsUi() }
                }
            }*/
    }


    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun onSeatClicked(seat: Seat, pos: Int) {
        if (seat.status == Status.Selected){
            selectedSeats.add(seat.num)
        }else {
           selectedSeats.remove(seat.num)
        }
        Toast.makeText(this.context,"Seats No. ${selectedSeats.toList()}",Toast.LENGTH_SHORT).show()

    }

    /*override fun handleView() {
        //binding.btnSeatConfirm.setOnClickListener { findNavController().navigate(SeatSelectionFragmentDirections.actionSearchResultsFragmentToSeatConfirmationFragment())}
        //binding.tvBusSelected.text = selectedSeats.toString()
    }*/

    /*override fun onSeatClicked(seat: Seat, pos: Int) {
        if (!seat.isSelected){
            if (selectedSeats.contains(seat.num)){
                selectedSeats.remove(seat.num)
                Toast.makeText(this.context,"Seats $selectedSeats",Toast.LENGTH_SHORT).show()
            }else{
                selectedSeats.add(seat.num)
                Toast.makeText(this.context,"Seats $selectedSeats",Toast.LENGTH_SHORT).show()
            }
        }
    }*/


}