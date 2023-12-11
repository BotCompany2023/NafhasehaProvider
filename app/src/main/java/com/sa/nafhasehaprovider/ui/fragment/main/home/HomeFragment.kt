package com.sa.nafhasehaprovider.ui.fragment.main.home

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.sa.nafhasehaprovider.BuildConfig
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.NewOrderAdapter
import com.sa.nafhasehaprovider.adapter.NewOrderSocketAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.entity.response.acceptedOrRejectedOfferSocketResponse.AcceptedOrRejectedOfferSocketResponse
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder
import com.sa.nafhasehaprovider.entity.response.getNewOrder.ResponseNewOrder
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.interfaces.AcceptedOrRejectedOffer
import com.sa.nafhasehaprovider.interfaces.NewOrder
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import com.sa.nafhasehaprovider.network.soketManager.SocketManager
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.HomeViewModel
import com.sa.nafhasehaprovider.viewModels.InfoViewModel
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : BaseFragment<com.sa.nafhasehaprovider.databinding.FragmentHomeBinding>(),
    OrderDetails, NewOrder, AcceptedOrRejectedOffer {

    override fun getLayoutId(): Int = R.layout.fragment_home
    private var id_Order: Int=0
    private var position: Int = 0
    private lateinit var AvgRate: String
    private val viewModel: HomeViewModel by viewModel()
    private val ordersViewModel: OrdersViewModel by viewModel()
    private val infoViewModel: InfoViewModel by viewModel()


    lateinit var newOrderSocketAdapter: NewOrderSocketAdapter
    lateinit var listNewOrderSocket: ArrayList<ResponseNewOrder>
    lateinit var newOrderAdapter: NewOrderAdapter
    lateinit var listNewOrder: ArrayList<NewOrderHomeResponse>

    lateinit var  mActivity:MainActivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity=requireActivity() as MainActivity
        ConnectToSocket()

        NafhasehaProviderApp.pref.putBoolean(FIRST_TIME, true)

        onClick()



    }


    private fun initResponse() {

        listNewOrderSocket = ArrayList()
        newOrderSocketAdapter = NewOrderSocketAdapter(requireActivity(), listNewOrderSocket, this);
        mViewDataBinding.rvNewOrderSocket.adapter = newOrderSocketAdapter




        //response getHome
        viewModel.home(1, 50)
        viewModel.homeResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                AvgRate=it.data!!.provider.avg_rate
                                NafhasehaProviderApp.pref.putString(PROVIDER_RATING, AvgRate)

                                if (it.data!!.provider.image != null){
                                    Utilities.onLoadImageFromUrl(
                                        requireActivity(),
                                        it.data!!.provider.image!!,
                                        mViewDataBinding.ivLogoUser
                                    )
                                }

                                mViewDataBinding.tvNameProvider.text = it.data.provider.name
                                mViewDataBinding.tvAvgRate.text = it.data.provider.avg_rate
                                mViewDataBinding.tvCountJob.text =
                                    it.data.provider.count_orders_completed.toString()


                                listNewOrder = ArrayList()
                                newOrderAdapter = NewOrderAdapter(requireActivity(), listNewOrder, this);
                                listNewOrder.addAll(it.data!!.new_orders)
                                mViewDataBinding.rvNewOrder.adapter = newOrderAdapter
                                newOrderAdapter.notifyDataSetChanged()

                                if (listNewOrder.size == 0) {
                                    mViewDataBinding.constraintNoOrder.visibility = View.VISIBLE
                                } else {
                                    mViewDataBinding.constraintNoOrder.visibility = View.GONE

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
                    showProgress(false)

                }
            }
        })


        //response cancelOrderOngoing
        viewModel.cancelOrderOngoingResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                listNewOrder.removeAt(position)
                                newOrderAdapter.notifyDataSetChanged()

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


        //response acceptedOrderOngoing
        ordersViewModel.acceptedOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                val action = HomeFragmentDirections.actionMenuHomeToShowOrderFragment(id_Order)
                                mActivity.navController.navigate(action)

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


        // api response update app
        infoViewModel.versionUpdate("Android", BuildConfig.VERSION_NAME)
        infoViewModel.versionUpdateResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                if (it.data==true){

                                }
                                else{
                                    showDialogUpdateApp()
                                }
                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.showToastSuccess(requireActivity(), it.message)
                            }
                            CODE405 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                                showProgress(false)
                            }
                            CODE500 -> {
                                Utilities.showToastError(requireActivity(), it.message)
                            }
                            else -> {
                                Utilities.showToastError(requireActivity(), it.message)
                                showProgress(false)
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
                    showProgress(false)

                }
            }
        })



    }


    private fun onClick() {
    }

    override fun sendOrderId(idOrder: Int) {
        val action = HomeFragmentDirections.actionMenuHomeToShowOrderFragment(idOrder)
        mActivity.navController.navigate(action)
    }

    override fun cancelOrderId(idOrder: Int, pot: Int) {
        position=pot
        viewModel.cancelOrderOngoing(idOrder)

    }

    override fun acceptOrder(idOrder: Int, pot: Int) {
        ordersViewModel.acceptedOrder(idOrder)
        id_Order=idOrder
    }

    override fun sendOffer(idOrder: Int, offerPrice: String) {
        val action = HomeFragmentDirections.actionMenuHomeToBottomSheetAddOfferFragment(idOrder,AvgRate,offerPrice,"HOME_PAGE")
        mActivity.navController.navigate(action)
    }

    override fun trackingUser(
        orderId:Int,
        userID: Int,
        orderLat: Float,
        orderLong: Float,
        userImage: String,
        userName: String,
        userPhone: String,
        distance: String,
        estimatedTime: String
    ) {

    }


    fun ConnectToSocket() {
        SocketRepository.socketManager = SocketManager()
        SocketRepository.socketManager?.tryToReconnect()
        SocketRepository.socketManager?.onGetAcceptedOrRejectedOffers()
        SocketRepository.socketManager?.acceptedOrRejectedOffer = this
        SocketRepository.socketManager?.dataNewOrder = this
    }

    override fun newOrder(model: GetNewOrder) {
        requireActivity().runOnUiThread {
            Log.d("newOrderEE:", "SSFKKFKFF")
//            listNewOrderSocket.addAll(listOf(model.response))
            listNewOrderSocket.add(model.response)
            newOrderSocketAdapter.notifyDataSetChanged()
        }
    }


    override fun acceptedOrRejectedOffer(model: AcceptedOrRejectedOfferSocketResponse) {
        initResponse()
    }

    override fun onResume() {
        super.onResume()
        initResponse()
    }


    private fun showDialogUpdateApp() {
        var dialogDietReport = Dialog(requireActivity(), R.style.customDialogTheme)
        dialogDietReport.setCancelable(false)
        val inflater = this.layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_update_app, null)
        dialogDietReport.setContentView(v)

        val btnUpdate = v.findViewById<Button>(R.id.btn_update)
        val imageClose = v.findViewById<ImageView>(R.id.iv_imageClose)

        imageClose.setOnClickListener {
            dialogDietReport.dismiss()
        }
        btnUpdate.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireActivity().packageName}")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}")))
            }

        }

//        val window = dialogDietReport.window
//        window!!.setLayout(
//            ConstraintLayout.LayoutParams.MATCH_PARENT,
//            ConstraintLayout.LayoutParams.WRAP_CONTENT,)

        dialogDietReport.show()

    }


}