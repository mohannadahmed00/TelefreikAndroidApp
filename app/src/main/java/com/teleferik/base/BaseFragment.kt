package com.teleferik.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.teleferik.data.network.RemoteDataSource
import com.teleferik.data.ViewModelFactory
import com.teleferik.ui.LoadingDialog

abstract class BaseFragment<VM : ViewModel, B : ViewBinding, R : BaseRepo> : Fragment() {

    protected lateinit var binding: B
    protected val remoteDataSource = RemoteDataSource()
    lateinit var mViewModel: VM
    lateinit var loading: LoadingDialog
    var hasInitializedRootView = false
    private var rootView: View? = null

    fun getPersistentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            binding = getFragmentBinding(inflater!!, container, false)
            val factory= ViewModelFactory(getFragmentRepo())
            mViewModel= ViewModelProvider(this,factory)[getViewModel()]
            loading= LoadingDialog(requireActivity())
            handleView()
            rootView = binding.root
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                requireActivity().window.navigationBarColor =
                    ContextCompat.getColor(requireActivity(), com.teleferik.R.color.black);
            }
        } else {

            (rootView?.getParent() as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return getPersistentView(inflater, container, savedInstanceState)
    }


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean): B

    abstract fun getFragmentRepo(): R

    override fun onDestroyView() {
        super.onDestroyView()
        loading.cancel()
    }
    abstract fun handleView()
}

