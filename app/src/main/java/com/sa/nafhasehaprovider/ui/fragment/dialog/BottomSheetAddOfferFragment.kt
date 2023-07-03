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
import com.sa.nafhasehaprovider.ui.fragment.main.orders.ShowOrderFragmentArgs
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetAddOfferFragment : BaseBottomDialog<FragmentBottomSheetAddOfferBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_add_offer

    private var idOrder: Int=0
    private val viewModel: OrdersViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            val args: BottomSheetAddOfferFragmentArgs = BottomSheetAddOfferFragmentArgs.fromBundle(requireArguments())
            viewModel.showOrder(args.idOrder)
            idOrder=args.idOrder
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
        mViewDataBinding.btnSend.setOnClickListener {
            val price=mViewDataBinding.tvPrice.text.toString()
            if (price.isEmpty())
            {
                mViewDataBinding.tvPrice.error=getString(R.string.write_your_bid)
            }
            else{
                viewModel.submitPriceOffer(idOrder,price)
            }
        }
    }


}