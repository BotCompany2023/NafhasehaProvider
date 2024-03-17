package com.sa.nafhasehaprovider.ui.fragment.main.setting

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.CODE200
import com.sa.nafhasehaprovider.common.CODE403
import com.sa.nafhasehaprovider.common.CODE405
import com.sa.nafhasehaprovider.common.CODE500
import com.sa.nafhasehaprovider.common.LANG
import com.sa.nafhasehaprovider.common.Resource
import com.sa.nafhasehaprovider.common.USER_DATA
import com.sa.nafhasehaprovider.common.shareLinkApp
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentSettingBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_setting
    private val viewModel: AuthenticationViewModel by viewModel()


    private lateinit var dialogChangeLanguage: Dialog
    var language = "ar"
    private var mainActivity: MainActivity? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        onClick()

        if (USER_DATA!=null)
        {
            mViewDataBinding.tvNameUser.text = NafhasehaProviderApp.pref.loadUserData(
                requireActivity(), USER_DATA
            )!!.data!!.provider!!.name
            Utilities.onLoadImageFromUrl(
                requireActivity(), NafhasehaProviderApp.pref.loadUserData(
                    requireActivity(), USER_DATA
                )!!.data!!.provider!!.image, mViewDataBinding.ivImageUser)
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
        viewModel.changeDefaultLanguageResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            CODE200 -> {
                                var intent = Intent(requireActivity(), MainActivity::class.java)
                                intent.putExtra("type", "SETTING")
                                startActivity(intent)
                                requireActivity().finishAffinity()

                                Animatoo.animateFade(requireActivity())
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

        mViewDataBinding.updateProfile.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToAccountFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.changePassword.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToChangePasswordFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }


        mViewDataBinding.aboutApp.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToAboutFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.contactUs.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToContactUsFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }


        mViewDataBinding.frequentlyAskedQuestions.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToFrequentlyQuestionsFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.privacyPolicy.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToPrivacyPolicyFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.termsAndConditions.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToTermsConditionsFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }
        mViewDataBinding.logOut.setOnClickListener {
            val action = SettingFragmentDirections.actionMenuSettingToBottomSheetLogOutFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }


        mViewDataBinding.language.setOnClickListener {
            showDialogChangeLanguage()
        }


        mViewDataBinding.shearing.setOnClickListener {
            shareLinkApp(requireActivity())
        }

        mViewDataBinding.rating.setOnClickListener {
            val packageName = requireActivity().packageName
            val uri = Uri.parse("market://details?id=$packageName")

            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

            try {
                requireActivity().startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // يمكن أن يحدث هذا الخطأ إذا لم يكن التطبيق مثبتًا على الجهاز
                // يمكنك التعامل مع هذا الحالة حسب احتياجاتك
                Toast.makeText(context, "تطبيق متجر جوجل غير مثبت", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun showDialogChangeLanguage() {
        dialogChangeLanguage = Dialog(requireActivity(), R.style.customDialogTheme)
        dialogChangeLanguage.setCancelable(true)
        val inflater = this.layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_change_language, null)
        dialogChangeLanguage.setContentView(v)

        var btnConfirm: AppCompatButton = v.findViewById(R.id.btn_confirm)
        var rbAr: ConstraintLayout = v.findViewById(R.id.constraint_ar)
        var rbEn: ConstraintLayout = v.findViewById(R.id.constraint_en)
        var ivAr: ImageView = v.findViewById(R.id.iv_chick_ar)
        var ivEn: ImageView = v.findViewById(R.id.iv_chick_en)
        var ivClose: ImageView = v.findViewById(R.id.iv_close_dialog)

        if ( Locale.getDefault().language =="ar")
        {
            ivAr.setImageResource(R.drawable.ic_radio_button_chick)
            ivEn.setImageResource(R.drawable.ic_radio_button_un_chick)

        }
        else{
            ivAr.setImageResource(R.drawable.ic_radio_button_un_chick)
            ivEn.setImageResource(R.drawable.ic_radio_button_chick)

        }


        if (NafhasehaProviderApp.pref.getString(LANG, "") == "ar") {
            ivAr.setImageResource(R.drawable.ic_radio_button_chick)
            ivEn.setImageResource(R.drawable.ic_radio_button_un_chick)
        } else if (NafhasehaProviderApp.pref.getString(LANG, "") == "en") {
            ivAr.setImageResource(R.drawable.ic_radio_button_un_chick)
            ivEn.setImageResource(R.drawable.ic_radio_button_chick)
        }

        ivClose.setOnClickListener {
            dialogChangeLanguage.dismiss()
        }

        rbAr.setOnClickListener {
            ivAr.setImageResource(R.drawable.ic_radio_button_chick)
            ivEn.setImageResource(R.drawable.ic_radio_button_un_chick)
            language = "ar"
        }

        rbEn.setOnClickListener {
            ivEn.setImageResource(R.drawable.ic_radio_button_chick)
            ivAr.setImageResource(R.drawable.ic_radio_button_un_chick)
            language = "en"
        }
        btnConfirm.setOnClickListener {
            if (language == "ar") {
                // make app language arabic
                NafhasehaProviderApp.pref.putString(LANG, "ar")
                // redirect to activity
                    viewModel.changeLanguage("ar")

                dialogChangeLanguage.dismiss()

            } else {
                // make app language english
                NafhasehaProviderApp.pref.putString(LANG, "en")
                    viewModel.changeLanguage("en")
                dialogChangeLanguage.dismiss()
            }

        }


        val window = dialogChangeLanguage.window
        window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        try {
            dialogChangeLanguage.show()
        } catch (e: Exception) {
        }
    }



}