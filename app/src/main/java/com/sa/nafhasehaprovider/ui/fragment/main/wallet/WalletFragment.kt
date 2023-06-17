package com.sa.nafhasehaprovider.ui.fragment.main.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.CreditWalletAdapter
import com.sa.nafhasehaprovider.adapter.DebitWalletAdapter
import common.*
import com.sa.nafhasehaprovider.common.util.RecyclerViewLoadMoreScroll
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.databinding.FragmentWalletBinding
import com.sa.nafhasehaprovider.entity.response.walletResponse.CreditWalletResponse
import com.sa.nafhasehaprovider.entity.response.walletResponse.DebitWalletResponse
import com.sa.nafhasehaprovider.interfaces.OnLoadMoreListener
import com.sa.nafhasehaprovider.viewModels.WalletViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WalletFragment : BaseFragment<FragmentWalletBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_wallet

    private val viewModel: WalletViewModel by viewModel()

    lateinit var debitWalletAdapter: DebitWalletAdapter
    lateinit var creditWalletAdapter: CreditWalletAdapter

    lateinit var listDebit: ArrayList<DebitWalletResponse>
    lateinit var  listCredit: ArrayList<CreditWalletResponse>

    lateinit var layoutManagerCredit: LinearLayoutManager
    lateinit var layoutManagerDebit: LinearLayoutManager
    lateinit var scrollListener: RecyclerViewLoadMoreScroll


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
        scrollListener = RecyclerViewLoadMoreScroll(layoutManagerCredit as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
               //viewModel.wallet(1,10)
            }
        })
        mViewDataBinding.rvDebit.addOnScrollListener(scrollListener)
    }

    private fun initResponse() {
        listDebit = ArrayList()
        listCredit = ArrayList()
        layoutManagerCredit = LinearLayoutManager(requireActivity())
        layoutManagerDebit = LinearLayoutManager(requireActivity())

        mViewDataBinding.rvDebit.layoutManager = layoutManagerDebit
        debitWalletAdapter =
            DebitWalletAdapter(requireActivity(), listDebit)

        mViewDataBinding.rvCredit.layoutManager = layoutManagerCredit
        creditWalletAdapter =
            CreditWalletAdapter(requireActivity(), listCredit)

        //response get my wallet
        viewModel.wallet(1, 50)
        viewModel.walletResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                mViewDataBinding.tvTotalWallet.text =
                                    it.data!!.my_wallet.toString() + "" + getString(R.string.sr)

                                listDebit.addAll(it.data.debit!!)
                                mViewDataBinding.rvDebit.adapter = debitWalletAdapter


                                listCredit.addAll(it.data.credit!!)
                                mViewDataBinding.rvCredit.adapter = creditWalletAdapter


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
        mViewDataBinding.btnChargeNow.setOnClickListener {

        }

        mViewDataBinding.btnWithdrawNow.setOnClickListener {

        }
    }


}