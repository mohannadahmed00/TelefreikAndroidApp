package com.teleferik.ui.privateTrip.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teleferik.ui.home.HomeFragment
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.searchAirPorts.SearchAriPortsFragment
import com.teleferik.ui.home.searchCities.SearchCitiesFragment
import com.teleferik.ui.notifications.NotificationFragment

internal class TabsAdapter(supportFragmentManager: FragmentManager,private val totalTabs:Int) :
    FragmentPagerAdapter(supportFragmentManager) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomeFragment()
            }
            1 -> {
                HomeFragment()

            }
            2 -> {
                HomeFragment()
            }
            else ->                 HomeFragment()

        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

}