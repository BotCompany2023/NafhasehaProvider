package com.sa.nafhasehaprovider.ui.fragment.auth.forgetPassword

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
import com.sa.nafhasehaprovider.databinding.FragmentResetPasswordBinding
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_reset_password

    private lateinit var mobile: String
    private var userId: Int? = null
    private val viewModel: AuthenticationViewModel by viewModel()

    var phone: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()

        if (arguments != null) {
            val args: ResetPasswordFragmentArgs =
                ResetPasswordFragmentArgs.fromBundle(requireArguments())
            mobile = args.mobile
            userId = args.userId
        }


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()
            Utilities.dismissDialogNoInternet()
        }
        else{
            Utilities.showDialogNoInternet(requireActivity())
        }

    }

    private fun initResponse() {
        // resend response
        viewModel.checkPhoneResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                val action =
                                    ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment()
                                mViewDataBinding.root.findNavController().navigate(action)
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

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    val action =
                        ResetPasswordFragmentDirections.actionResetPasswordFragmentToVerifyOtpFragment(
                            mobile, userId!!, ""
                        )
                    mViewDataBinding.root.findNavController().navigate(action)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        mViewDataBinding.ivBack.setOnClickListener {
            val action =
                ResetPasswordFragmentDirections.actionResetPasswordFragmentToVerifyOtpFragment(
                    mobile, userId!!, ""
                )
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.btnSave.setOnClickListener {

            val newPassword = mViewDataBinding.tvNewPassword.text.toString().trim()
            val confirmPassword = mViewDataBinding.tvConfirmPassword.text.toString().trim()

            if (newPassword.isEmpty()) {
                mViewDataBinding.tvNewPassword.error = getString(R.string.new_password_is_required)
            } else if (mViewDataBinding.tvNewPassword.length() < 4) {
                mViewDataBinding.tvNewPassword.error =
                    getString(R.string.the_password_is_not_less_than_four_letters_numbers_or_letters_and_numbers)
            } else if (confirmPassword.isEmpty()) {
                mViewDataBinding.tvConfirmPassword.error =
                    getString(R.string.confirm_password_is_required)
            } else if (mViewDataBinding.tvConfirmPassword.length() < 4) {
                mViewDataBinding.tvConfirmPassword.error =
                    getString(R.string.the_password_is_not_less_than_four_letters_numbers_or_letters_and_numbers)
            } else {
                viewModel.resetPass(userId!!, newPassword)
            }
        }

    }


}