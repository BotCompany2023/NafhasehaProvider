package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetAddOfferBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetRatingBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetRechargeBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetWithdrawBinding
import com.sa.nafhasehaprovider.ui.fragment.main.orders.ShowOrderFragmentArgs
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetWithdrawFragment : BaseBottomDialog<FragmentBottomSheetWithdrawBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_withdraw

    private val viewModel: OrdersViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            val args:BottomSheetWithdrawFragmentArgs= BottomSheetWithdrawFragmentArgs.fromBundle(requireArguments())
           mViewDataBinding.tvTotalWallet.text=""+args.amount +getString(R.string.sr)
        }



        initResponse()
        onClick()

    }

    private fun initResponse() {
        //response submitPriceOffer
        viewModel.submitPriceOfferResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                dismiss()
                                Utilities.showToastSuccess(requireActivity(), it.message)
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
        mViewDataBinding.btnChargeNow.setOnClickListener {
//            val price=mViewDataBinding.tvAmount.text.toString()
//            if (price.isEmpty())
//            {
//                mViewDataBinding.tvAmount.error=getString(R.string.tell_us_the_shipping_amount)
//            }
//            else{
//                //viewModel.submitPriceOffer(idOrder,price)
//            }
        }
    }


}