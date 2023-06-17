package com.sa.nafhasehaprovider.ui.fragment.main.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.NewOrderAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentHomeBinding
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.interfaces.ClickItemService
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import com.sa.nafhasehaprovider.interfaces.PackageDetails
import com.sa.nafhasehaprovider.ui.fragment.auth.login.LoginFragmentDirections
import com.sa.nafhasehaprovider.viewModels.HomeViewModel
import common.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(),OrderDetails {

    override fun getLayoutId(): Int = R.layout.fragment_home
    private val viewModel: HomeViewModel by viewModel()

    lateinit var newOrderAdapter: NewOrderAdapter
    lateinit var  listNewOrder: ArrayList<NewOrderHomeResponse>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        NafhasehaProviderApp.pref.putBoolean(FIRST_TIME, true)


        onClick()

        initResponse()
    }


    private fun initResponse() {

        listNewOrder=ArrayList()
        newOrderAdapter= NewOrderAdapter(requireActivity(),listNewOrder,this);


        //response getHome
        viewModel.home(1,50)
        viewModel.homeResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Utilities.onLoadImageFromUrl(requireActivity(),
                                it.data!!.provider.image,
                                mViewDataBinding.ivLogoUser)
                                mViewDataBinding.tvNameProvider.text=it.data.provider.name
                                mViewDataBinding.tvAvgRate.text=it.data.provider.avg_rate
                                mViewDataBinding.tvCountRate.text= it.data.provider.rates_count.toString()
                                mViewDataBinding.tvCountJob.text= it.data.provider.count_orders_completed.toString()

                                listNewOrder.addAll(it.data.new_orders)
                                mViewDataBinding.rvNewOrder.adapter=newOrderAdapter
                                newOrderAdapter.notifyDataSetChanged()

                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.showToastError(requireActivity(), it.message)
                                Utilities.logOutApp(requireActivity())

                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                        }

                    }

                }
                is Resource.Error -> {
                    // dismiss loading
                    showProgress(false)
                    Log.i("TestVerification", "error")

                }
                is Resource.Loading -> {
                    // show loading
                    Log.i("TestVerification", "loading")
                    showProgress(true)

                }
            }
        })


    }


    private fun onClick() {
    }

    override fun sendOrderId(idOrder: Int) {
        val action = HomeFragmentDirections.
        actionMenuHomeToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }


}