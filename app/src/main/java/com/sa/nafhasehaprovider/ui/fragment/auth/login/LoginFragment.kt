package com.sa.nafhasehaprovider.ui.fragment.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
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
//                                NafhasehaProviderApp.pref.authToken = it.data!!.access_token
//                                NafhasehaProviderApp.pref.saveUserData(
//                                    requireActivity(), USER_DATA, it)
//                                NafhasehaProviderApp.pref.putInt(USER_ID, it.data!!.provider!!.id!!)

//                                NafhasehaProviderApp.pref.saveUserData(requireActivity(),USER_DATA,it)




                                if (it.data!!.provider!!.is_activation == false) {
                                    val action =
                                        LoginFragmentDirections.actionLoginFragmentToVerifyOtpFragment(
                                            mViewDataBinding.tvMobile.text.toString(),
                                            it.data!!.provider!!.id!!,
                                            "LOGIN"
                                        )
                                    mViewDataBinding.root.findNavController().navigate(action)
                                } else {
                                    openActivityAndFinish(MainActivity::class.java)
                                    NafhasehaProviderApp.pref.saveUserData(requireActivity(),USER_DATA,it)
                                    NafhasehaProviderApp.pref.authToken =it.data!!.access_token
                                    NafhasehaProviderApp.pref.putInt(USER_ID, it.data!!.provider!!.id)

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



        // استخدام TextWatcher لمراقبة التغييرات في نص المدخل
        mViewDataBinding.tvMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // لاحاجة لتنفيذ أي شيء هنا
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // لاحاجة لتنفيذ أي شيء هنا
            }

            override fun afterTextChanged(s: Editable?) {
                // تحقق إذا كان أول حرف هو 5 وإذا لم يكن قم بحذفه
                if (!s.isNullOrEmpty() && s[0] != '5') {
                    s.delete(0, 1) // حذف الحرف الأول
                    // عرض رسالة تنبيه للمستخدم
                    Toast.makeText(requireActivity(), getString(R.string.the_mobile_number_must_start_with_the_number_5), Toast.LENGTH_SHORT).show()
                }
            }
        })

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