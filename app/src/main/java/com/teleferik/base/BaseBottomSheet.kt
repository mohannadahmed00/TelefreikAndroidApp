package com.teleferik.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teleferik.R


abstract class BaseBottomSheet< B : ViewBinding> : BottomSheetDialogFragment() {
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,  R.style.BottomSheetTheme);

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  getFragmentBinding(inflater, container,false)
        return  binding.root
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean):B
}