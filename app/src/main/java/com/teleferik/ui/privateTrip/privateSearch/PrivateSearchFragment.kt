package com.teleferik.ui.privateTrip.privateSearch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teleferik.R
import com.teleferik.base.BaseFragment
import com.teleferik.data.network.apisInterfaces.ApisService
import com.teleferik.databinding.FragmentPrivateSearchBinding
import com.teleferik.databinding.FragmentPrivateTripBinding
import com.teleferik.databinding.FragmentSearchCitiesBinding
import com.teleferik.ui.home.searchCities.SearchCitiesFragmentArgs
import com.teleferik.ui.privateTrip.ExpandableListDataPump
import com.teleferik.ui.privateTrip.PrivateRepo
import com.teleferik.ui.privateTrip.PrivateTripViewModel
import com.teleferik.ui.privateTrip.adapters.StationsAdapter
import com.teleferik.utils.Constants

class PrivateSearchFragment : BaseFragment<PrivateTripViewModel, FragmentPrivateSearchBinding, PrivateRepo>() {

    private val args: PrivateSearchFragmentArgs by navArgs()
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean
    ): FragmentPrivateSearchBinding = FragmentPrivateSearchBinding.inflate(layoutInflater)

    override fun getViewModel(): Class<PrivateTripViewModel> {
        return PrivateTripViewModel::class.java
    }

    override fun getFragmentRepo(): PrivateRepo {
        return PrivateRepo(remoteDataSource.buildApi(ApisService::class.java))
    }

    override fun handleView() {
        val expandableListDetail = ExpandableListDataPump.getData()
        val expandableListTitle = expandableListDetail.keys.toList()
        val adapter = StationsAdapter(context,expandableListTitle,expandableListDetail)
        binding.includePrivateSearch.exLvStations.setAdapter(adapter)
        binding.includePrivateSearch.exLvStations.setOnGroupExpandListener {
            Toast.makeText(context, expandableListTitle[it] + "List Expanded", Toast.LENGTH_SHORT).show()
        }
        binding.includePrivateSearch.exLvStations.setOnGroupCollapseListener {
            Toast.makeText(context, expandableListTitle[it] + "List Collapsed", Toast.LENGTH_SHORT).show()
        }
        binding.includePrivateSearch.exLvStations.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            if (args.isSearchFromStart){
                setFragmentResult("start", bundleOf("station" to expandableListDetail[expandableListTitle[groupPosition]]?.get(childPosition)))
            }else{
                setFragmentResult("end", bundleOf("station" to expandableListDetail[expandableListTitle[groupPosition]]?.get(childPosition)))
            }
            binding.includePrivateSearch.edtSearch.postDelayed(Runnable {
                val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                keyboard!!.hideSoftInputFromWindow(binding.includePrivateSearch.edtSearch.windowToken, 0)
                findNavController().navigateUp()
            }, 200)
            //Toast.makeText(context, expandableListTitle[groupPosition] + " -> " + expandableListDetail[expandableListTitle[groupPosition]]?.get(childPosition), Toast.LENGTH_SHORT).show()
            false
        }
        binding.imgBack.setOnClickListener{findNavController().navigateUp()}
        if (args.stringStation != "-") binding.includePrivateSearch.edtSearch.setText(args.stringStation)

    }
}