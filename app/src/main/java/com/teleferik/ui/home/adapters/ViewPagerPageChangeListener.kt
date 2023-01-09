package com.teleferik.ui.home.adapters

import android.content.Context
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewpager.widget.ViewPager
import com.teleferik.utils.addViewPagerBottomDots

abstract class ViewPagerPageChangeListener(
    private val mContext: Context,
    private val mLength: Int,
    private val mLinearLayoutCompat: LinearLayoutCompat
) : ViewPager.OnPageChangeListener {
    override fun onPageSelected(position: Int) {
        addViewPagerBottomDots(mContext, position, mLength, 1,mLinearLayoutCompat)
    }
}