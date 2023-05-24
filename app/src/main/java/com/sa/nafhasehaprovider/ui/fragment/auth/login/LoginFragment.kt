package com.sa.nafhasehaprovider.ui.fragment.auth.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentLoginBinding
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_login

    private val viewModel: AuthenticationViewModel by viewModel()

    var phone: String = ""
    var password: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initResponse()

    }


    private fun initResponse() {
        // resend response
        viewModel.authResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)

                                // saving token
                                NafhasehaProviderApp.pref.authToken = it.data!!.access_token
                                NafhasehaProviderApp.pref.saveUserData(
                                    requireActivity(), USER_DATA, it)

//                                NafhasehaApp.pref.putString(USER_ID, it.data!!.user!!.id!!.toString())
//                                NafhasehaApp.pref.putString(USER_NAME, it.data!!.user!!.name!!)


                                if (it.data!!.user!!.is_activation == false) {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToVerifyOtpFragment(
                                            mViewDataBinding.tvMobile.text.toString(),
                                            it.data!!.user!!.id!!,
                                            "LOGIN"
                                        )
                                    mViewDataBinding.root.findNavController().navigate(action)
                                } else {
                                    openActivityAndFinish(MainActivity::class.java)

                                }

                                Utilities.showToastSuccess(requireActivity(), it.message)


//                                GlobalContext.unloadKoinModules(
//                                    listOf(
//                                     //   welcomeViewModelModule,
//                                        repoModule,
//                                        sharedPreferencesModule,
//                                        appModule,
//                                        authenticationViewModelModule
//                                    )
//                                )
//                                loadKoinModules(
//                                    listOf(
//                                    //    welcomeViewModelModule,
//                                        repoModule,
//                                        sharedPreferencesModule,
//                                        appModule,
//                                        authenticationViewModelModule
//                                    )
//                                )
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
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        mViewDataBinding.btnLogin.setOnClickListener {

            phone = mViewDataBinding.tvMobile.text.toString().trim()
            password = mViewDataBinding.tvPassword.text.toString().trim()

            if (phone.isEmpty()) {
                mViewDataBinding.tvMobile.error = getString(R.string.mobile_number_is_required)
            } else if (password.isEmpty()) {
                mViewDataBinding.tvPassword.error = getString(R.string.password_is_required)
            } else if (mViewDataBinding.tvPassword.length() < 4) {
                mViewDataBinding.tvPassword.error =
                    getString(R.string.the_password_is_not_less_than_four_letters_numbers_or_letters_and_numbers)
            } else {
                viewModel.login(phone, password)
            }

        }

        mViewDataBinding.tvCreateANewAccount.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.tvForgetPassword.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMobileForgetPasswordFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }
    }


}