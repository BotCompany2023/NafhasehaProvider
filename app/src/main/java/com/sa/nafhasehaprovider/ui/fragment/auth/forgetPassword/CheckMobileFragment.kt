package com.sa.nafhasehaprovider.ui.fragment.auth.forgetPassword

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
import com.sa.nafhasehaprovider.databinding.FragmentCheckMobileBinding
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckMobileFragment : BaseFragment<FragmentCheckMobileBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_check_mobile

    private val viewModel: AuthenticationViewModel by viewModel()

    var phone: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
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
                                    CheckMobileFragmentDirections.actionMobileForgetPasswordFragmentToVerifyOtpFragment(
                                        mViewDataBinding.tvMobile.text.toString(),
                                        it.data!!, "CHECK_MOBILE_FORGET")
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
                        CheckMobileFragmentDirections.actionMobileForgetPasswordFragmentToLoginFragment()
                    mViewDataBinding.root.findNavController().navigate(action)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        mViewDataBinding.ivBack.setOnClickListener {
            val action =
                CheckMobileFragmentDirections.actionMobileForgetPasswordFragmentToLoginFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.btnSendCode.setOnClickListener {

            phone = mViewDataBinding.tvMobile.text.toString().trim()

            if (phone.isEmpty()) {
                mViewDataBinding.tvMobile.error = getString(R.string.mobile_number_is_required)
            } else {
                viewModel.checkPhone(phone, 1)
            }
        }

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

}