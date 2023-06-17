package com.sa.nafhasehaprovider.ui.fragment.auth.chaingPassword

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.databinding.FragmentChaingPasswordBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.fragment.auth.forgetPassword.ResetPasswordFragmentArgs
import com.sa.nafhasehaprovider.ui.fragment.auth.forgetPassword.ResetPasswordFragmentDirections
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChangePasswordFragment : BaseFragment<FragmentChaingPasswordBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_chaing_password

    private val viewModel: AuthenticationViewModel by viewModel()
    private var mainActivity: MainActivity? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE


        onClick()
        initResponse()

    }


    private fun initResponse() {
        // resend response
        viewModel.changePasswordResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                val action =
                                   ChangePasswordFragmentDirections.actionChangePasswordFragmentToMenuSetting()
                                mViewDataBinding.root.findNavController().navigate(action)
                                onDestroy()
                            }
                            CODE403 -> {
                                //unAuthorized()
                                NafhasehaProviderApp.pref.clearSharedPref()
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

        mViewDataBinding.ivBack.setOnClickListener {
            val action =
                ChangePasswordFragmentDirections.actionChangePasswordFragmentToMenuSetting()
            mViewDataBinding.root.findNavController().navigate(action)
            onDestroy()
        }

        mViewDataBinding.btnUpdate.setOnClickListener {

            val oldPassword = mViewDataBinding.tvOldPassword.text.toString().trim()
            val newPassword = mViewDataBinding.tvNewPassword.text.toString().trim()

            if (oldPassword.isEmpty()) {
                mViewDataBinding.tvOldPassword.error = getString(R.string.enter_the_current_password)
            }
            else if (newPassword.isEmpty()) {
                mViewDataBinding.tvNewPassword.error = getString(R.string.new_password_is_required)
            }
           else if (mViewDataBinding.tvNewPassword.length() < 4) {
                mViewDataBinding.tvNewPassword.error =
                    getString(R.string.the_password_is_not_less_than_four_letters_numbers_or_letters_and_numbers)
            } else {
                viewModel.changePassword(oldPassword, newPassword)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }


}