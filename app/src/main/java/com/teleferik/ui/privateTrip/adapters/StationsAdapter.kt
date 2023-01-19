package com.teleferik.ui.privateTrip.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.teleferik.R

class StationsAdapter(val context: Context?,private val expandableListTitle: List<String>, private val expandableListDetail: Map<String,List<String>>): BaseExpandableListAdapter() {
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]!![expandedListPosition]
    }


    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }


    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val listTitle = getGroup(listPosition) as String
        if (view == null) {
            val layoutInflater = this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.row_air_ports_search_results, null)
        }
        val listTitleTextView = view?.findViewById<TextView>(R.id.tvName)
        listTitleTextView?.setCompoundDrawables(null,null,null,null)
        listTitleTextView?.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        listTitleTextView?.setTypeface(null, Typeface.BOLD)
        listTitleTextView?.text = listTitle
        return view!!
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val expandedListText = getChild(listPosition,expandedListPosition) as String
        if (view == null) {
            val layoutInflater =  this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.row_air_ports_search_results,null)
        }
        val expandedListTextView =view?.findViewById<TextView>(R.id.tvName)
        expandedListTextView?.text = expandedListText
        return view!!
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

}