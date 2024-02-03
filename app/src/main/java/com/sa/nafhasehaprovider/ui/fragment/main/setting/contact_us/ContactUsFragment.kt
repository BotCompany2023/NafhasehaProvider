package com.sa.nafhasehaprovider.ui.fragment.main.setting.contact_us

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.SocialMediaAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentContactUsBinding
import com.sa.nafhasehaprovider.entity.response.iconSocialMediaResponse.DataIconSocialMediaResponse
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_contact_us
    private var title: String = ""
    private var mainActivity: MainActivity? = null

    private val viewModel: InfoViewModel by viewModel()

    lateinit var socialMediaAdapter: SocialMediaAdapter
    lateinit var listIcons: ArrayList<DataIconSocialMediaResponse>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        onClick()

        mViewDataBinding.tvName.setText(
            NafhasehaProviderApp.pref.loadUserData(
                requireActivity(), USER_DATA
            )!!.data!!.provider!!.name
        )
        mViewDataBinding.tvMobile.setText(
            NafhasehaProviderApp.pref.loadUserData(
                requireActivity(), USER_DATA
            )!!.data!!.provider!!.phone
        )


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

        listIcons = ArrayList()
        socialMediaAdapter = SocialMediaAdapter(requireActivity(), listIcons)

        // resend response
        viewModel.socialMedia()
        viewModel.socialMediaResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listIcons.addAll(it.data!!)
                                mViewDataBinding.rvSocialMedia.adapter = socialMediaAdapter
                                socialMediaAdapter.notifyDataSetChanged()

                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.logOutApp(requireActivity())
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

        // resend response contactUs
        viewModel.contactUsResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                mViewDataBinding.tvNotes.text.clear()
                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.logOutApp(requireActivity())
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
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

        mViewDataBinding.ivProfileOrBack.setOnClickListener {
            val action = ContactUsFragmentDirections.actionContactUsFragmentToMenuSetting()
            mViewDataBinding.root.findNavController().navigate(action)
            onDestroy()
        }

        mViewDataBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_complaint -> {
                    title = getString(R.string.complaint)
                }
                R.id.rb_suggestion -> {
                    title = getString(R.string.suggestion)
                }
            }
        }

        mViewDataBinding.btnSend.setOnClickListener {

            val phone = mViewDataBinding.tvMobile.text.toString()
            val notes = mViewDataBinding.tvNotes.text.toString()
            if (title == "") {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.please_specify_the_title_of_the_letter_complaint_or_suggestion)
                )
            } else if (phone.isEmpty()) {
                mViewDataBinding.tvMobile.error = getString(R.string.mobile_number_is_required)
            } else if (notes.isEmpty()) {
                mViewDataBinding.tvNotes.error = getString(R.string.type_the_text_of_the_message)
            } else {
                viewModel.contactUs(title, 1, phone, notes)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }


}