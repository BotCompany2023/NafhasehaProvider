package com.sa.nafhasehaprovider.ui.fragment.auth.verify

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentVerifyOtpBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyOtpFragment : BaseFragment<FragmentVerifyOtpBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_verify_otp
    private val viewModel: AuthenticationViewModel by viewModel()

    private lateinit var mobile: String
    private var userId: Int? = null
    private lateinit var typePage: String
    var countDownTimer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initResponse()
        countDownTimer()

        if (arguments != null) {
            val args: VerifyOtpFragmentArgs = VerifyOtpFragmentArgs.fromBundle(requireArguments())
            mViewDataBinding.tvPhone.text = args.mobile
            typePage = args.typePage
            userId = args.userId
            mobile = args.mobile
        }

    }

    private fun initResponse() {
        // resend response
        viewModel.checkPhoneResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                if (typePage == "REGISTER") {
                                    startActivity(
                                        Intent(
                                            requireActivity(), MainActivity::class.java
                                        )
                                    )
                                    requireActivity().finish()
                                } else if (typePage == "CHECK_MOBILE") {
                                    val action =
                                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToResetPasswordFragment(
                                            mobile, userId!!
                                        )
                                    mViewDataBinding.root.findNavController().navigate(action)
                                } else if (typePage == "LOGIN") {
                                    val action =
                                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                                    mViewDataBinding.root.findNavController().navigate(action)
                                }

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

        // resend response send Activation code
        viewModel.sendActivationCodeResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)

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
                    if (typePage == "REGISTER") {
                        val action =
                            VerifyOtpFragmentDirections.actionVerifyOtpFragmentToRegisterFragment()
                        mViewDataBinding.root.findNavController().navigate(action)
                    } else if (typePage == "CHECK_MOBILE") {
                        val action =
                            VerifyOtpFragmentDirections.actionVerifyOtpFragmentToMobileForgetPasswordFragment()
                        mViewDataBinding.root.findNavController().navigate(action)
                    } else if (typePage == "LOGIN") {
                        val action =
                            VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                        mViewDataBinding.root.findNavController().navigate(action)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        mViewDataBinding.ivBack.setOnClickListener {
            if (typePage == "REGISTER") {
                val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToRegisterFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            } else if (typePage == "CHECK_MOBILE") {
                val action =
                    VerifyOtpFragmentDirections.actionVerifyOtpFragmentToMobileForgetPasswordFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            } else if (typePage == "LOGIN") {
                val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            }
        }

        mViewDataBinding.otpBtnLogin.setOnClickListener {

            val otp = mViewDataBinding.otpView.otp.toString().trim()

            if (otp.isEmpty()) {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.please_enter_the_code_sent_to_the_mobile_number)
                )
            } else {
                viewModel.checkCode(userId!!, otp)
            }
        }

        mViewDataBinding.tvResendTheCode.setOnClickListener {

            if (typePage == "REGISTER") {
                viewModel.sendActivationCode(userId!!, "Activation")
            } else if (typePage == "CHECK_MOBILE") {
                viewModel.sendActivationCode(userId!!, "Reset ")
            }


        }


    }

    fun countDownTimer() {
        countDownTimer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var sec = millisUntilFinished / 1000
                mViewDataBinding.counter.text = "00:$sec"

//                mViewDataBinding.sendAgain.setTextColor(
//                    activity?.resources!!.getColor(
//                        R.color.text,
//                        null
//                    )
//                )
                mViewDataBinding.tvResendTheCode.isClickable = false
                mViewDataBinding.tvResendTheCode.isEnabled = false
                mViewDataBinding.tvResendTheCode.alpha = 0.5F

                //here you can have your logic to set text to edittext
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onFinish() {
                try {
                    mViewDataBinding.tvResendTheCode.setTextColor(
                        activity?.resources!!.getColor(
                            R.color.appColor, null
                        )
                    )

                    mViewDataBinding.tvResendTheCode.isClickable = true
                    mViewDataBinding.tvResendTheCode.isEnabled = true
                    mViewDataBinding.tvResendTheCode.alpha = 1F

                } catch (e: Exception) {
                }


            }
        }.start()
    }


}