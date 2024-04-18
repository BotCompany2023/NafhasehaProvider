package com.sa.nafhasehaprovider.base

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.sa.nafhasehaprovider.common.util.NetworkReceiver
import com.sa.nafhasehaprovider.interfaces.ConnectivityListener
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository

abstract class BaseFragment<T : ViewDataBinding> : Fragment() , ConnectivityListener {
    lateinit var mViewDataBinding: T
    protected var activity: BaseActivity<*>? = null

    private lateinit var networkReceiver: NetworkReceiver


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this.activity = context
        }
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(
            inflater, getLayoutId(), container, false
        )
        //   mViewDatamViewDataBinding.lifecycleOwner = viewLifecycleOwner
        networkReceiver = NetworkReceiver(this)
        requireActivity().registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


        return mViewDataBinding.root
    }


    fun showProgress(it: Boolean) {
        if (it) showProgressDialog()
        else hideProgressDialog()

    }

    fun showProgressDialog() {
        activity?.showProgressDialog()
    }

    fun hideProgressDialog() {
        activity?.hideProgressDialog()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
        activity?.openActivity(destinationActivity, bundle)
    }

    fun openActivityAndFinish(destinationActivity: Class<*>, bundle: Bundle? = null) {
        activity?.openActivityAndFinish(destinationActivity, bundle)

    }

    fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

//    fun onBack() {
//        view?.let { activity?.onBackPressed() }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        // قم بإلغاء تسجيل المستمع هنا في onDestroyView
        requireActivity().unregisterReceiver(networkReceiver)
    }



}