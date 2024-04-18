package com.sa.nafhasehaprovider.ui.fragment.main.wallet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.CreditWalletAdapter
import com.sa.nafhasehaprovider.adapter.DebitWalletAdapter
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

    private var totalWallet: Double? =null
    private val viewModel: WalletViewModel by viewModel()

    lateinit var debitWalletAdapter: DebitWalletAdapter
    lateinit var creditWalletAdapter: CreditWalletAdapter

    lateinit var listDebit: ArrayList<DebitWalletResponse>
    lateinit var  listCredit: ArrayList<CreditWalletResponse>

    lateinit var layoutManagerCredit: LinearLayoutManager
    lateinit var layoutManagerDebit: LinearLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()

        //** Set the scrollListerner of the RecyclerView

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
                                totalWallet=it.data!!.my_wallet!!
                                mViewDataBinding.tvTotalWallet.text =
                                    it.data!!.my_wallet.toString() + "" + getString(R.string.sr)

                                listDebit.addAll(it.data.debit!!)
                                mViewDataBinding.rvDebit.adapter = debitWalletAdapter


//                                listCredit.addAll(it.data.credit!!)
//                                mViewDataBinding.rvCredit.adapter = creditWalletAdapter


                                if (listDebit.size==0){
                                    mViewDataBinding.constraintLayout8.visibility=View.GONE
                                }
                                else{
                                    mViewDataBinding.constraintLayout8.visibility=View.VISIBLE

                                }

                                if (totalWallet != null){
                                    mViewDataBinding.btnWithdrawNow.visibility=View.VISIBLE
                                }

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

                val action = WalletFragmentDirections.
                actionMenuWalletToBottomSheetRechargeFragment()
                mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.btnWithdrawNow.setOnClickListener {
            val action = WalletFragmentDirections.
            actionMenuWalletToBottomSheetWithdrawFragment(totalWallet!!.toInt())
            mViewDataBinding.root.findNavController().navigate(action)
        }
    }


}