package com.sa.nafhasehaprovider.ui.fragment.main.setting.fq

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.FaqsTitleAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentFrequentlyQuestionsBinding
import com.sa.nafhasehaprovider.entity.response.fqResponse.FaqsResponseData
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.FaqsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FrequentlyQuestionsFragment : BaseFragment<FragmentFrequentlyQuestionsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_frequently_questions
    private var mainActivity: MainActivity? = null

    private val viewModel: FaqsViewModel by viewModel()

    lateinit var faqsTitleAdapter: FaqsTitleAdapter
    var listFaqsTitle = ArrayList<FaqsResponseData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

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
        viewModel.faqs()
        viewModel.faqsResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            // dismiss loading
                            CODE200 -> {
                                //Utilities.showToastSuccess(requireActivity(), it.message)
                                faqsTitleAdapter =
                                    FaqsTitleAdapter(requireActivity(), listFaqsTitle)
                                listFaqsTitle.addAll(it.data)
                                mViewDataBinding.rvQuestionsAndAnswer.adapter = faqsTitleAdapter


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
            val action =
                FrequentlyQuestionsFragmentDirections.actionFrequentlyQuestionsFragmentToMenuSetting()
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