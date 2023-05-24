package com.sa.nafhasehaprovider.ui.fragment.main.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.TransactionsWalletAdapter
import common.*
import com.sa.nafhasehaprovider.common.util.RecyclerViewLoadMoreScroll
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.databinding.FragmentWalletBinding
import com.sa.nafhasehaprovider.entity.response.walletResponse.TransactionWalletResponse
import com.sa.nafhasehaprovider.interfaces.OnLoadMoreListener
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.WalletViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : BaseFragment<FragmentWalletBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_wallet

    private val viewModel: WalletViewModel by viewModel()

    lateinit var transactionsWalletAdapter: TransactionsWalletAdapter
    lateinit var listTransactions: ArrayList<TransactionWalletResponse>

    lateinit var layoutManager: LinearLayoutManager
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var referralCode: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
        initResponse()

        //** Set the scrollListerner of the RecyclerView
        setRVScrollListener()
    }


    // initScrollListener() method is the method where we are checking
    // the scrolled state of the RecyclerView and if bottom-most is visible
    // we are showing the loading view and populating the next list
    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(layoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                //  viewModel.wallet(1,10)

            }
        })
        mViewDataBinding.rvTransactions.addOnScrollListener(scrollListener)
    }

    private fun initResponse() {
        listTransactions = ArrayList()
        layoutManager = LinearLayoutManager(requireActivity())

        //response get my wallet
        viewModel.wallet(1, 50)
        viewModel.walletResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    listTransactions.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                referralCode = it.data!!.invite_code
                                mViewDataBinding.tvTotalWallet.text =
                                    it.data!!.my_wallet.toString() + "" + getString(R.string.sr)
                                mViewDataBinding.tvCode.text = it.data!!.invite_code
                                mViewDataBinding.rvTransactions.layoutManager = layoutManager
                                transactionsWalletAdapter =
                                    TransactionsWalletAdapter(requireActivity(), listTransactions)
                                listTransactions.addAll(it.data.transactions!!)
                                mViewDataBinding.rvTransactions.adapter = transactionsWalletAdapter


                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.showToastError(requireActivity(), it.message)
                                Utilities.logOutApp(requireActivity())

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
        mViewDataBinding.tvShareCode.setOnClickListener {
            shareApp(
                requireActivity(), referralCode,
                NafhasehaProviderApp.pref.loadUserData(requireActivity(), USER_DATA)!!
                    .data!!.user!!.name!!
            )
        }
    }


}