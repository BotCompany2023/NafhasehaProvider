package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.DropDownBanksAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.showToastError
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetAddOfferBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetRatingBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetRechargeBinding
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetWithdrawBinding
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponseData
import com.sa.nafhasehaprovider.entity.response.getBanksResponse.GetBanksResponseData
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.fragment.main.orders.ShowOrderFragmentArgs
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import com.sa.nafhasehaprovider.viewModels.WalletViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetWithdrawFragment : BaseBottomDialog<FragmentBottomSheetWithdrawBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_withdraw

    private lateinit var name: String
    private lateinit var Iban: String
    private lateinit var price: String
    private lateinit var mess: String
    private var totalWallet: Int=1
    private var idBank: Int=0
    private val viewModel: WalletViewModel by viewModel()

    lateinit var dropDownBanksAdapter: DropDownBanksAdapter
    lateinit var dataSource: ArrayList<GetBanksResponseData>

    lateinit var mActivity:MainActivity


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity=requireActivity() as MainActivity

        if (arguments != null) {
            val args:BottomSheetWithdrawFragmentArgs= BottomSheetWithdrawFragmentArgs.fromBundle(requireArguments())
           mViewDataBinding.tvTotalWallet.text=""+args.amount +getString(R.string.sr)
            totalWallet=args.amount


            if (totalWallet>0){
                mViewDataBinding.btnSendRequest.visibility=View.VISIBLE
            }

        }



        initResponse()
        onClick()

    }



    private fun initResponse() {

        dataSource=ArrayList()
        dropDownBanksAdapter =
            DropDownBanksAdapter(requireActivity(), dataSource)

        dataSource.add(GetBanksResponseData(0,  getString(R.string.select)))

        viewModel.getBanks()
        viewModel.banksResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                dataSource.addAll(it.data)
                                mViewDataBinding.spAllBank.adapter = dropDownBanksAdapter
                                dropDownBanksAdapter.notifyDataSetChanged()

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



        viewModel.requestWithdrawalResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message)
                                dismiss()
                                mActivity.navController!!.popBackStack()

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

        mViewDataBinding.spAllBank?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        idBank = dataSource[position].id
                    } else {

                    }
                }

            }


        mViewDataBinding.btnSendRequest.setOnClickListener {


            name=mViewDataBinding.tvName.text.toString()
             Iban=mViewDataBinding.tvIbanNumber.text.toString()
            price=mViewDataBinding.tvWithdrawalAmount.text.toString()
            val validationResult = validateBankAccountAndIBAN(requireContext(),Iban)
//            Toast.makeText(requireActivity(), Iban, Toast.LENGTH_SHORT).show()


            if (idBank==0)
            {
                showToastError(requireActivity(),getString(R.string.please_select_a_bank))
            }
            else if (name.isEmpty())
            {
                mViewDataBinding.tvName.error=getString(R.string.name_is_required)
            }
            else if (Iban.isEmpty())
            {
                mViewDataBinding.tvIbanNumber.error=getString(R.string.iban_number)
            }

            else if (price.isEmpty())
            {
                mViewDataBinding.tvWithdrawalAmount.error=getString(R.string.tell_us_the_shipping_amount)
            }
            else if (mViewDataBinding.tvWithdrawalAmount.text.toString().toInt() ==0)
            {
                showToastError(requireActivity(),getString(R.string.enter_a_valid_amount))
            }
            else if (mViewDataBinding.tvWithdrawalAmount.text.toString().toInt() > totalWallet)
            {
                showToastError(requireActivity(),getString(R.string.your_balance_is_insufficients))
            }
            else{

                viewModel.requestWithdrawal(idBank,name,Iban,price.toInt())


//                if (validationResult == getString(R.string.valid)) {
//                    // الرقم صحيح، قم بتنفيذ الدالة المعينة هنا
//                    viewModel.requestWithdrawal(idBank,name,Iban,price.toInt())
//                } else {
//                    // الرقم غير صحيح، يمكنك هنا إظهار رسالة للمستخدم أو إجراء إجراءات أخرى
//                    showToastError(requireActivity(),validationResult)
//                }


            }
        }
    }


    fun validateBankAccountAndIBAN(context: Context, accountNumber: String): String {
        val bankAccountPattern = "^SA\\d{22}$".toRegex()
        val ibanPattern = "^\\d{14}$".toRegex()

        return if (!bankAccountPattern.matches(accountNumber) && !ibanPattern.matches(accountNumber)) {
            context.getString(R.string.invalid_account_iban)
        } else if (!bankAccountPattern.matches(accountNumber)) {
            context.getString(R.string.invalid_account)
        } else if (!ibanPattern.matches(accountNumber)) {
            context.getString(R.string.invalid_iban)
        } else {
            context.getString(R.string.valid)
        }
    }


}