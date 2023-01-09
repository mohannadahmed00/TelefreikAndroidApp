package com.teleferik.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.teleferik.data.network.RemoteDataSource
import com.teleferik.ui.LoadingDialog

abstract class BaseBindingFragment< B : ViewBinding> : Fragment() {

    protected lateinit var binding: B
    protected val remoteDataSource = RemoteDataSource()
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
            loading= LoadingDialog(requireActivity())
            handleView()
            rootView = binding.root
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




    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean): B


    override fun onDestroyView() {
        super.onDestroyView()
        loading.cancel()
    }
    abstract fun handleView()


}

