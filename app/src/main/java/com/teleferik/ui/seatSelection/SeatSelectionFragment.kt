package com.teleferik.ui.seatSelection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentSeatSelectionBinding
import com.teleferik.models.seats.Seat
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.seatSelection.compose.SeatsUi

@ExperimentalFoundationApi
class SeatSelectionFragment :
    BaseFragment<HomeViewModel, FragmentSeatSelectionBinding, HomeRepo>() {
    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    /*private fun getFakeData():MutableList<Seat>{
        val seats = mutableListOf<Seat>()
        for (i in 1..28) {
            if (i in arrayOf(2, 5, 9, 12)) {
                seats.add(Seat(num = i, isSelected = true, status = "reserved"))
            } else {
                seats.add(Seat(num = i, isSelected = false,status = "available"))
            }
        }
        return seats
    }
*/
    //var selectedSeats = mutableListOf<Int>()


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ) = FragmentSeatSelectionBinding.inflate(layoutInflater)

    override fun handleView() {
        initClicks()

    }
    private fun initClicks() {
        binding.imgBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnConfirm.setOnClickListener { findNavController().navigate(SeatSelectionFragmentDirections.actionSeatSelectionFragmentToSeatConfirmationFragment()) }
        binding.composeSeatsView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                MaterialTheme {
                    SeatsUi()
                }
            }
        }
    }


    override fun getFragmentRepo(): HomeRepo {
        return HomeRepo(remoteDataSource.buildApi(ApisService::class.java))
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