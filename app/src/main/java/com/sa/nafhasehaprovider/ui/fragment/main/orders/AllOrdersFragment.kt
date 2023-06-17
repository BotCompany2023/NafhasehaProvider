package com.sa.nafhasehaprovider.ui.fragment.main.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.AllOrderAdapter
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentAllOrdersBinding
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.entity.response.getAllOrdersResponse.DataAllOrdersResponse
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import com.sa.nafhasehaprovider.ui.fragment.main.home.HomeFragmentDirections

class AllOrdersFragment : BaseFragment<FragmentAllOrdersBinding>(),OrderDetails {

    override fun getLayoutId(): Int = R.layout.fragment_all_orders
    private val viewModel: OrdersViewModel by viewModel()

    lateinit var allOrderApprovAdapter: AllOrderAdapter
    lateinit var orderApprovedList: ArrayList<DataAllOrdersResponse>
    lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        initResponse()

    }

    private fun initResponse() {
        orderApprovedList = ArrayList()
        layoutManager = LinearLayoutManager(requireActivity())
        mViewDataBinding.rvAllOrder.layoutManager = layoutManager
        allOrderApprovAdapter = AllOrderAdapter(requireActivity(), orderApprovedList,this)


        //response ordersApproved
        viewModel.ordersApproved(1,50)
        viewModel.allOrdersApprovedResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    orderApprovedList.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                orderApprovedList.addAll(it.data!!)
                                mViewDataBinding.rvAllOrder.adapter = allOrderApprovAdapter
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


        //response ordersCompleted
        viewModel.allOrdersCompletedResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    orderApprovedList.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                orderApprovedList.addAll(it.data!!)
                                mViewDataBinding.rvAllOrder.adapter = allOrderApprovAdapter
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
        mViewDataBinding.btnNewOrder.setOnClickListener {
            viewModel.ordersApproved(1,50)

            mViewDataBinding.btnNewOrder.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvNewOrder.setTextColor(resources.getColor(R.color.white))
            mViewDataBinding.ivNewOrder.setImageResource(R.drawable.ic_new_order)

            mViewDataBinding.btnExpiredOrder.setBackgroundDrawable(null)
            mViewDataBinding.tvExpiredOrder.setTextColor(resources.getColor(R.color.grey5))
            mViewDataBinding.ivExpiredOrder.setImageResource(R.drawable.ic_finch_order2)

        }

        mViewDataBinding.btnExpiredOrder.setOnClickListener {
            viewModel.ordersCompleted(1,50)

            mViewDataBinding.btnExpiredOrder.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvExpiredOrder.setTextColor(resources.getColor(R.color.white))
            mViewDataBinding.ivExpiredOrder.setImageResource(R.drawable.ic_finch_order)

            mViewDataBinding.btnNewOrder.setBackgroundDrawable(null)
            mViewDataBinding.tvNewOrder.setTextColor(resources.getColor(R.color.grey5))
            mViewDataBinding.ivNewOrder.setImageResource(R.drawable.ic_new_order2)


        }
    }

    override fun sendOrderId(idOrder: Int) {
        val action = AllOrdersFragmentDirections.
        actionMenuOrdersToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }


}