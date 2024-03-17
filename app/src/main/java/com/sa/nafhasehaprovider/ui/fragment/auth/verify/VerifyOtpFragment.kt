package com.sa.nafhasehaprovider.ui.fragment.auth.verify

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentVerifyOtpBinding
import com.sa.nafhasehaprovider.ui.activity.AuthActivity
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import `in`.aabhasjindal.otptextview.OTPListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class VerifyOtpFragment : BaseFragment<FragmentVerifyOtpBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_verify_otp
    private val viewModel: AuthenticationViewModel by viewModel()

    private lateinit var mobile: String
    private var userId: Int? = null
    private lateinit var typePage: String
    var countDownTimer: CountDownTimer? = null

    lateinit var authActivity: AuthActivity




    private val REQ_USER_CONSENT=200
    private var smsBroadcastReceive: SmsBroadcastReceiver? = null
//    private var intentFilter: IntentFilter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        countDownTimer()

        // Init Sms Retriever >>>>
        initSmsListener()
//        initBroadCast()

        authActivity=requireActivity() as AuthActivity

        if (arguments != null) {
            val args: VerifyOtpFragmentArgs = VerifyOtpFragmentArgs.fromBundle(requireArguments())
            typePage = args.typePage
            userId = args.userId
            mobile = args.mobile
            mViewDataBinding.tvPhone.text="+966"+mobile
        }

    }

    private fun initResponse() {

        if (typePage == "CHECK_MOBILE_FORGET") {

            // resend response
            viewModel.checkCodeResetResponse.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Resource.Success -> {
                        // dismiss loading
                        showProgress(false)
                        result.data?.let { it ->
                            when (it.code) {

                                CODE200 -> {
                                    Utilities.showToastSuccess(requireActivity(), it.message)

                                    val action =
                                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToResetPasswordFragment(
                                            mobile, userId!!)
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
        else{

            // resend response
            viewModel.checkCodeResponse.observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Resource.Success -> {
                        // dismiss loading
                        showProgress(false)
                        result.data?.let { it ->
                            when (it.code) {

                                CODE200 -> {
                                    Utilities.showToastSuccess(requireActivity(), it.message)

                                    // saving token
                                    NafhasehaProviderApp.pref.authToken =it.data!!.access_token
                                    NafhasehaProviderApp.pref.saveUserData(requireActivity(),USER_DATA,it)

                                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                                    requireActivity().finish()


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
                                countDownTimer()
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
                    val callback: OnBackPressedCallback =
                        object : OnBackPressedCallback(true /* enabled by default */) {
                            override fun handleOnBackPressed() {
                                // Handle the back button event
                                if (typePage == "REGISTER") {
                                    val action =
                                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToRegisterFragment()
                                    mViewDataBinding.root.findNavController().navigate(action)
                                } else if (typePage == "CHECK_MOBILE_FORGET") {
                                    val action =
                                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                                    mViewDataBinding.root.findNavController().navigate(action)
                                } else if (typePage == "LOGIN") {
                                    val action =
                                        VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                                    mViewDataBinding.root.findNavController().navigate(action)
                                }
                            }
                        }
                    requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


        mViewDataBinding.ivBack.setOnClickListener {
            if (typePage == "REGISTER") {
                val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToRegisterFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            } else if (typePage == "CHECK_MOBILE_FORGET") {
                val action =
                    VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            } else if (typePage == "LOGIN") {
                val action = VerifyOtpFragmentDirections.actionVerifyOtpFragmentToLoginFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            }
        }

        mViewDataBinding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                if (typePage == "CHECK_MOBILE_FORGET") {
                    viewModel.checkCodeReset(userId!!, otp)

                }
                else{
                    viewModel.checkCode(userId!!, otp)
                }


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
                if (typePage == "CHECK_MOBILE_FORGET") {
                    viewModel.checkCodeReset(userId!!, otp)

                }
                else{
                    viewModel.checkCode(userId!!, otp)
                }
            }
        }

        mViewDataBinding.tvResendTheCode.setOnClickListener {

            if (typePage == "REGISTER") {
                viewModel.sendActivationCode(userId!!, "Activation")
            } else if (typePage == "LOGIN") {
                viewModel.sendActivationCode(userId!!, "Reset")
            }


        }


    }

    fun countDownTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
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

            override fun onFinish() {
                try {
                    mViewDataBinding.tvResendTheCode.setTextColor(
                        activity?.resources!!.getColor(
                            R.color.black, null
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

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(requireActivity())
        client.startSmsUserConsent(null)

    }


    var smsActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            //That gives all message to us.
            // We need to get the code from inside with regex

            val message = data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            getOtpFromMessage(message!!)
            Log.d("smsAhmed", message.toString())

        }
    }

    private fun getOtpFromMessage(message: String?) {
        // This will match any 6 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{4}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            Log.d("smsAhmed", matcher.group(0))
            mViewDataBinding.otpView.otp = matcher.group(0)
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceive=SmsBroadcastReceiver()
        smsBroadcastReceive!!.smsBroadcastReceiveListener= object :SmsBroadcastReceiver.
        SmsBroadcastReceiveListener {
            override fun onSuccess(intent: Intent?) {
                // startActivityForResult(requireActivity().intent,REQ_USER_CONSENT)
                smsActivityResultLauncher.launch(intent)
            }

            override fun onFailure() {
            }

        }
        initBroadCast()
    }

    private fun initBroadCast() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(smsBroadcastReceive, intentFilter,
                Context.RECEIVER_EXPORTED
            )
        }else {
            requireActivity(). registerReceiver(smsBroadcastReceive, intentFilter)
        }

    }


    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(smsBroadcastReceive)
    }

    override fun onDestroy() {
        super.onDestroy()
        smsBroadcastReceive = null
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