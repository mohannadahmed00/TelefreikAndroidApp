package com.teleferik.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.teleferik.data.ViewModelFactory
import com.teleferik.data.network.RemoteDataSource
import com.teleferik.ui.LoadingDialog
import com.teleferik.R


abstract class BaseBottomSheetViewModel<VM : ViewModel, B : ViewBinding, R : BaseRepo> : BottomSheetDialogFragment() {


    protected lateinit var binding: B
    protected val remoteDataSource = RemoteDataSource()
    lateinit var mViewModel: VM
    lateinit var loading: LoadingDialog
    var hasInitializedRootView = false


    private var rootView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,com.teleferik.R.style.BottomSheetTheme);

    }

    fun getPersistentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            binding = getFragmentBinding(inflater!!, container, false)
            val factory= ViewModelFactory(getFragmentRepo())
            mViewModel= ViewModelProvider(this,factory).get(getViewModel())
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


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean): B

    abstract fun getFragmentRepo(): R

    override fun onDestroyView() {
        super.onDestroyView()
        loading.cancel()
    }
    abstract fun handleView()


}
