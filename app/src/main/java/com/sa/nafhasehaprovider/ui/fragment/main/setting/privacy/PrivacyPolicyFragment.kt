package com.sa.nafhasehaprovider.ui.fragment.main.setting.privacy

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentPrivacyPolicyBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.sa.nafhasehaprovider.base.BaseFragment

class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_privacy_policy
    private var mainActivity: MainActivity? = null

    private val viewModel: InfoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        onClick()
        initResponse()
    }

    private fun initResponse() {
        // resend response
        viewModel.info("privacyPolicy")
        viewModel.infoResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            // dismiss loading
                            CODE200 -> {
                                //    Utilities.showToastSuccess(requireActivity(), it.message)
                                mViewDataBinding.tvText.setHtml(it.data.description)
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
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

        mViewDataBinding.ivProfileOrBack.setOnClickListener {
            val action = PrivacyPolicyFragmentDirections.actionAboutFragmentToMenuSetting()
            mViewDataBinding.root.findNavController().navigate(action)
            onDestroy()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }


}