package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.nafhasehaprovider.entity.response.canceledReasonsResponse.DataCanceledReasonsResponse
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.CanceledReasonsAdapter
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetCancelOrderBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetCancelOrderFragment : BaseBottomDialog<FragmentBottomSheetCancelOrderBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_cancel_order
    private val viewModel: OrdersViewModel by viewModel()


    lateinit var canceledReasonsAdapter: CanceledReasonsAdapter
    lateinit var listCanceledReasons: ArrayList<DataCanceledReasonsResponse>

    private lateinit var mainActivity: MainActivity
    private var orderID: Int=0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity

        if (arguments != null) {
            val args: BottomSheetCancelOrderFragmentArgs =
                BottomSheetCancelOrderFragmentArgs.fromBundle(requireArguments())
            orderID= args.idOrder
        }

        initResponse()
        onClick()

    }



    private fun initResponse() {

        //response getCanceledReasons
        viewModel.getCanceledReasons()
        viewModel.getCanceledReasonsResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                listCanceledReasons = ArrayList()

                                val layoutManager = LinearLayoutManager(requireActivity())
                                mViewDataBinding.rvReason.layoutManager = layoutManager
                                canceledReasonsAdapter = CanceledReasonsAdapter(requireActivity(), listCanceledReasons)
                                listCanceledReasons.addAll(it.data!!)
                                mViewDataBinding.rvReason.adapter = canceledReasonsAdapter

//                                if (listOffersApp.size == 0) {
//                                    mViewDataBinding.ivNoData.visibility = View.VISIBLE
//                                } else {
//                                    mViewDataBinding.ivNoData.visibility = View.GONE
//                                }


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
                    showProgress(false)
                }
            }
        })

        //response CancelOrder
        viewModel.cancelOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                mainActivity.navController!!.navigate(R.id.menuOrders)
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                dismiss()
                                mainActivity.mViewDataBinding.bottomNav.visibility = View.VISIBLE
                                mainActivity.mViewDataBinding.toolbar.visibility = View.VISIBLE
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
                    showProgress(false)
                }
            }
        })



    }


    private fun onClick() {
        mViewDataBinding.btnSend.setOnClickListener {
            if (canceledReasonsAdapter.idReason==0){
                Utilities.showToastError(requireActivity(), getString(R.string.What_is_the_reason_for_rejection))
            }
            else{
                viewModel.cancelOrder(orderID,canceledReasonsAdapter.idReason)
            }

        }
    }


}