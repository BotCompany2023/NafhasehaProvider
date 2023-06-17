package com.sa.nafhasehaprovider.ui.fragment.main.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.databinding.FragmentShowOrderBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity

class ShowOrderFragment : BaseFragment<FragmentShowOrderBinding>() {

    override fun getLayoutId(): Int =R.layout.fragment_show_order

    private var mainActivity: MainActivity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }


}