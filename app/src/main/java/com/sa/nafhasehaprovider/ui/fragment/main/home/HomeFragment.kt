package com.sa.nafhasehaprovider.ui.fragment.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.NewOrderAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentHomeBinding
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder
import com.sa.nafhasehaprovider.entity.response.getNewOrder.ResponseNewOrder
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.interfaces.NewOrder
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import com.sa.nafhasehaprovider.network.soketManager.SocketManager
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository.mediaPlayer
import com.sa.nafhasehaprovider.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(), OrderDetails, NewOrder {

    override fun getLayoutId(): Int = R.layout.fragment_home
    private val viewModel: HomeViewModel by viewModel()

    lateinit var newOrderAdapter: NewOrderAdapter
    lateinit var listNewOrder: ArrayList<ResponseNewOrder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ConnectToSocket()

        NafhasehaProviderApp.pref.putBoolean(FIRST_TIME, true)

        onClick()

        initResponse()

    }


    private fun initResponse() {

        listNewOrder = ArrayList()
        newOrderAdapter = NewOrderAdapter(requireActivity(), listNewOrder, this);


        //response getHome
        viewModel.home(1, 50)
        viewModel.homeResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.provider.image,
                                    mViewDataBinding.ivLogoUser
                                )
                                mViewDataBinding.tvNameProvider.text = it.data.provider.name
                                mViewDataBinding.tvAvgRate.text = it.data.provider.avg_rate
                                mViewDataBinding.tvCountJob.text =
                                    it.data.provider.count_orders_completed.toString()


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
        val action = HomeFragmentDirections.actionMenuHomeToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }


    fun ConnectToSocket() {
        SocketRepository.socketManager = SocketManager()
        SocketRepository.socketManager?.tryToReconnect()
        SocketRepository.socketManager?.dataNewOrder = this

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        if (newOrderAdapter != null) {
//            SocketRepository.onDisconnect()
//        }
//    }
//    override fun onPause() {
//        super.onPause()
//        SocketRepository.onDisconnect()
//
//    }
    override fun newOrder(model: GetNewOrder) {
        Log.d("newOrderEE:","SSFKKFKFF")
        listNewOrder.addAll(listOf(model.response))
        mViewDataBinding.rvNewOrder.adapter = newOrderAdapter
        newOrderAdapter.notifyDataSetChanged()

    if (listNewOrder.size==0){
        mViewDataBinding.constraintNoOrder.visibility=View.VISIBLE
    }
    else{
        mViewDataBinding.constraintNoOrder.visibility=View.GONE

    }

        Toast.makeText(requireActivity(), "newOrderEE", Toast.LENGTH_SHORT).show()
    }


}