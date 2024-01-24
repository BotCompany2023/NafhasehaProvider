package com.sa.nafhasehaprovider.ui.fragment.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseBottomDialog
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentBottomSheetAddOfferBinding
import com.sa.nafhasehaprovider.entity.request.acceptOrder.AcceptOrderRequest
import com.sa.nafhasehaprovider.entity.request.sendOffer.SendOfferRequest
import com.sa.nafhasehaprovider.interfaces.SuccessEmit
import com.sa.nafhasehaprovider.network.soketManager.SocketManager
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetAddOfferFragment : BaseBottomDialog<FragmentBottomSheetAddOfferBinding>() ,
    SuccessEmit {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_add_offer

    private var priceType: Int?=null
    private lateinit var pageType: String
    private lateinit var priceUpdate: String
    private lateinit var priceOffer: String
    private lateinit var mainActivity: MainActivity
    private var idOrder: Int=0
    var avgRate: String = ""
    var image: String = ""
    var name: String = ""
    var numberPhone: String = ""
    var typeProvider: String = ""

    private val viewModel: OrdersViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity

        ConnectToSocket()

        if (arguments != null) {
            val args: BottomSheetAddOfferFragmentArgs = BottomSheetAddOfferFragmentArgs.fromBundle(requireArguments())
            viewModel.showOrder(args.idOrder)
            idOrder=args.idOrder
             avgRate=args.avgRate
             priceOffer=args.offerPrice
             pageType=args.flagPage
            priceType=args.priceType
            mViewDataBinding.tvPrice.setText(priceOffer)
//             avgRate=NafhasehaProviderApp.pref.loadUserData(requireActivity(),USER_DATA)!!.data!!.provider!!.avg_rate!!

             name= NafhasehaProviderApp.pref.loadUserData(
                 requireActivity(), USER_DATA)!!.data!!.provider!!.name!!
             numberPhone=NafhasehaProviderApp.pref.loadUserData(
                 requireActivity(), USER_DATA)!!.data!!.provider!!.phone!!
             typeProvider=NafhasehaProviderApp.pref.loadUserData(
                 requireActivity(), USER_DATA)!!.data!!.provider!!.provider_type!!

       if (NafhasehaProviderApp.pref.loadUserData(
               requireActivity(), USER_DATA)!!.data!!.provider!!.image != null){
           image=NafhasehaProviderApp.pref.loadUserData(
               requireActivity(), USER_DATA)!!.data!!.provider!!.image!!
       }
        }


        initResponse()
        onClick()

    }

    private fun initResponse() {
        //response submitPriceOffer
        viewModel.submitPriceOfferResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
//                                if (pageType=="SHOW_ORDER_PAGE")
//                                {
//                                    dismiss()
//                                }
//                                else{
//                                    dismiss()
//                                    mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
//                                    mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
//                                }

                             //   Utilities.showToastSuccess(requireActivity(), it.message)



//                                if (pageType=="SHOW_ORDER_PAGE"){
//                                    val action = BottomSheetAddOfferFragmentDirections.
//                                    actionBottomSheetAddOfferFragmentToShowOrderFragment(idOrder)
//                                    mainActivity.navController.navigate(action)
//                                    //mainActivity.navController.popBackStack()
//
//                                    }
//
//                                else{
                                    val action = BottomSheetAddOfferFragmentDirections.
                                    actionBottomSheetAddOfferFragmentToMenuHome()
                                    mainActivity.navController!!.navigate(action)
                                dismiss()
                                mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
                                mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE

//                                }

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
        mViewDataBinding.btnSend.setOnClickListener {
            priceUpdate=mViewDataBinding.tvPrice.text.toString()
            if (priceUpdate.isEmpty())
            {
                mViewDataBinding.tvPrice.error=getString(R.string.write_your_bid)
            }
            else{

                val dataRequest = SendOfferRequest(
                    avgRate,
                    image,
                    name,
                    numberPhone,
                    idOrder,
                    priceUpdate.toInt(),
                    typeProvider,
                    priceType!!)
                if (dataRequest !=null)
                {
                    onPrintLog(onConvertObjToJson(dataRequest))
                    SocketRepository.onSendOfferRequest(dataRequest)
                    viewModel.submitPriceOffer(idOrder,priceUpdate)
                }

            }
        }

    }
    fun ConnectToSocket() {
        SocketRepository.socketManager = SocketManager()
        SocketRepository.socketManager?.tryToReconnect()
        SocketRepository.socketManager?.successEmit = this
    }


    override fun successEmit(success: Boolean) {
        requireActivity().runOnUiThread {
            Log.d("newOrderEEf:","SSFKKFKFF")
            if (success)
            {
                dismiss()
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
//        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
//    }
}