package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.logOutApp
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetLogoutBinding
import com.sa.nafhasehaprovider.ui.generalViewModel.LogOutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetLogOutFragment : BaseBottomDialog<FragmentBottomSheetLogoutBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_logout
    private val viewModel: LogOutViewModel by viewModel()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewDataBinding.tvNameUser.text =
            getString(R.string.hello) + " " + NafhasehaProviderApp.pref.loadUserData(
                requireActivity(), USER_DATA
            )!!.data!!.provider!!.name

        initResponse()
        onClick()
    }

    private fun onClick() {
        mViewDataBinding.ivClose.setOnClickListener {
            dismiss()
        }

        mViewDataBinding.btnConfirm.setOnClickListener {
            viewModel.logOut()
        }
    }


    private fun initResponse() {
        // resend response
        viewModel.logOutResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                dismiss()
                                logOutApp(requireActivity())

                            }
                            CODE403 -> {
                                //unAuthorized()
                                dismiss()
                                logOutApp(requireActivity())
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                                dismiss()
                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                                dismiss()
                            }
                            else -> {

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


}