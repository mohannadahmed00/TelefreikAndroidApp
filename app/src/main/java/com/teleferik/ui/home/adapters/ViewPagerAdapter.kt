package com.teleferik.ui.home.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.teleferik.R
import com.bumptech.glide.Glide
import com.teleferik.models.promotionalOffer.Offer

class ViewPagerAdapter(private var context: Context, private var layouts: MutableList<Int>, private var news: List<Offer>): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return news.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(layouts[0], container, false)
        val image : AppCompatImageView = view.findViewById(R.id.slideimage)
        val title : AppCompatTextView = view.findViewById(R.id.slidetitle)
        val content : AppCompatTextView = view.findViewById(R.id.slidebrief)
        Glide.with(view).load(news[position].image)
            .centerCrop()
            .placeholder(R.drawable.ic_logo_notification)
            .error(R.drawable.ic_logo_notification)
            .fallback(R.drawable.ic_logo_notification)
            .into(image)
        title.text = news[position].title
        content.text = news[position].brief
        container.addView(view)
        return view
    }
}