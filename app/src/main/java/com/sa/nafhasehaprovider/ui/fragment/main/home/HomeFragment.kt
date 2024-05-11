package com.sa.nafhasehaprovider.ui.fragment.main.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.IntentFilter
import android.icu.text.IDNA.Info
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sa.nafhasehaprovider.BuildConfig
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.NewOrderAdapter
import com.sa.nafhasehaprovider.adapter.NewOrderSocketAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.EndlessRecyclerViewScrollListener
import com.sa.nafhasehaprovider.common.util.NetworkReceiver
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentHomeBinding
import com.sa.nafhasehaprovider.di.ordersViewModel
import com.sa.nafhasehaprovider.entity.request.acceptOrder.AcceptOrderRequest
import com.sa.nafhasehaprovider.entity.response.acceptedOrRejectedOfferSocketResponse.AcceptedOrRejectedOfferSocketResponse
import com.sa.nafhasehaprovider.entity.response.getNewOrder.GetNewOrder
import com.sa.nafhasehaprovider.entity.response.getNewOrder.ResponseNewOrder
import com.sa.nafhasehaprovider.entity.response.homeResponse.NewOrderHomeResponse
import com.sa.nafhasehaprovider.interfaces.AcceptedOrRejectedOffer
import com.sa.nafhasehaprovider.interfaces.ConnectivityListener
import com.sa.nafhasehaprovider.interfaces.NewOrder
import com.sa.nafhasehaprovider.interfaces.OrderDetails
import com.sa.nafhasehaprovider.network.soketManager.SocketManager
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.viewModels.HomeViewModel
import com.sa.nafhasehaprovider.viewModels.InfoViewModel
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import gun0912.tedimagepicker.util.ToastUtil.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(),
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

    var currentPage : Int = 1
    var countPage =5
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private var layoutManager: LinearLayoutManager? = null




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewDataBinding.viewBack.visibility=View.VISIBLE


        mActivity=requireActivity() as MainActivity

        NafhasehaProviderApp.pref.putBoolean(FIRST_TIME, true)

        onClick()

        layoutManager = LinearLayoutManager(requireContext())

        endlessRecyclerViewScrollListener=object : EndlessRecyclerViewScrollListener(layoutManager!!){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                currentPage=page+1
                viewModel.home(currentPage, countPage)
               // Toast.makeText(requireContext(), ""+page, Toast.LENGTH_SHORT).show()
            }
        }
        mViewDataBinding.rvNewOrder.addOnScrollListener(endlessRecyclerViewScrollListener)


        ConnectToSocket()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initResponse() {



//        listNewOrderSocket = ArrayList()
//        newOrderSocketAdapter = NewOrderSocketAdapter(requireActivity(), listNewOrderSocket, this);
      //  mViewDataBinding.rvNewOrderSocket.adapter = newOrderSocketAdapter

        listNewOrder = ArrayList()
        newOrderAdapter = NewOrderAdapter(requireActivity(), listNewOrder, this);
        mViewDataBinding.rvNewOrder.layoutManager = layoutManager



        //response getHome
        viewModel.home(currentPage, countPage)
        viewModel.getHomeResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
//                    listNewOrder.clear()
                    showProgress(false)
                    mViewDataBinding.viewBack.visibility=View.GONE

                    // listNewOrder.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                if (it.data!!.provider.is_active == 1) {
                                    mViewDataBinding.constraintActivation.visibility = View.GONE
                                    mActivity.mViewDataBinding.toolbar.visibility=View.VISIBLE
                                    mActivity.mViewDataBinding.bottomNav.visibility=View.VISIBLE
                                }
                                else {
                                    mViewDataBinding.constraintActivation.visibility = View.VISIBLE
                                    mActivity.mViewDataBinding.toolbar.visibility=View.GONE
                                    mActivity.mViewDataBinding.bottomNav.visibility=View.GONE
                                }

                                AvgRate=it.data!!.provider.avg_rate
                                NafhasehaProviderApp.pref.putString(PROVIDER_RATING, AvgRate)

                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.provider.image,
                                    mViewDataBinding.ivLogoUser
                                )
                                mViewDataBinding.tvNameProvider.text = it.data.provider.name
                                mViewDataBinding.tvAvgRate.text = it.data.provider.avg_rate
                                mViewDataBinding.tvCountJob.text =
                                    it.data.provider.count_orders_completed.toString()
                                mViewDataBinding.tvTotalWallet.text =
                                    it.data.my_wallet +""+getString(R.string.sar)





                                listNewOrder.addAll(it.data.new_orders)
                                mViewDataBinding.rvNewOrder.adapter = newOrderAdapter
                                newOrderAdapter.notifyDataSetChanged()





                                if (it.data.is_have_order!! ==true)
                                {
                                    mViewDataBinding.constraintIsHaveOrder.visibility=View.VISIBLE
                                    mActivity.mViewDataBinding.ivOnOff.setImageResource(R.drawable.icon_off)
                                    mActivity. mViewDataBinding.tvTitleStatus.text=getText(R.string.uavailable)
                                    mActivity.mViewDataBinding.constraintSwitch.isEnabled=false
                                    mViewDataBinding.constraintNoOrder.visibility=View.GONE
                                    mViewDataBinding.textView.visibility=View.GONE


                                }
                                else{
                                    mViewDataBinding.constraintIsHaveOrder.visibility=View.GONE
//                                    mActivity.mViewDataBinding.ivOnOff.setImageResource(R.drawable.icon_on)
//                                    mActivity.mViewDataBinding.tvTitleStatus.text=getText(R.string.available)
                                    mActivity.mViewDataBinding.constraintSwitch.isEnabled=true
                                    mViewDataBinding.constraintNoOrder.visibility=View.GONE
                                    mViewDataBinding.textView.visibility=View.VISIBLE

                                    if (listNewOrder.size!=0){
                                        mViewDataBinding.constraintNoOrder.visibility=View.GONE
                                    }
                                    else {
                                        mViewDataBinding.constraintNoOrder.visibility=View.VISIBLE
                                    }
                                }

                                infoViewModel.versionUpdate("Android", BuildConfig.VERSION_NAME)



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


        //response cancelOrderOngoing
        viewModel.cancelOrderOngoingResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

//                                listNewOrder.removeAt(position)
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
                                mActivity.navController!!.navigate(action)
                                val model= AcceptOrderRequest(id_Order)
                                SocketRepository.onAcceptOrderRequest(model)

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
        infoViewModel.versionUpdateResponse.observe(viewLifecycleOwner, Observer { result ->
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




    }


    private fun onClick() {
    }

    override fun sendOrderId(idOrder: Int) {
        val action = HomeFragmentDirections.actionMenuHomeToShowOrderFragment(idOrder)
        mActivity.navController!!.navigate(action)
    }

    override fun cancelOrderId(idOrder: Int, pot: Int) {
        position=pot
        viewModel.cancelOrderOngoing(idOrder)
        listNewOrder.removeAt(pot)
        newOrderAdapter.notifyDataSetChanged()
    }

    override fun acceptOrder(idOrder: Int, pot: Int) {
        ordersViewModel.acceptedOrder(idOrder)
        id_Order=idOrder
    }

    override fun sendOffer(idOrder: Int, offerPrice: String,priceType:Int) {
        val action = HomeFragmentDirections.
        actionMenuHomeToBottomSheetAddOfferFragment(idOrder,AvgRate,offerPrice,"HOME_PAGE",priceType)
        mActivity.navController!!.navigate(action)
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
        estimatedTime: String) {

    }

    override fun submitReports(idOrder: Int) {

    }


    fun ConnectToSocket() {
        SocketRepository.socketManager?.onGetAcceptedOrRejectedOffers()
        SocketRepository.socketManager?.acceptedOrRejectedOffer = this
        SocketRepository.socketManager?.dataNewOrder = this
    }

    override fun newOrder(model: GetNewOrder) {
        requireActivity().runOnUiThread {

            Log.d("newOrderEE:", "SSFKKFKFF")
//           // listNewOrderSocket.addAll(listOf(model.response))
//            listNewOrderSocket.add(model.response)
//            newOrderSocketAdapter.notifyDataSetChanged()
//            mViewDataBinding.constraintNoOrder.visibility = View.GONE

             viewModel.home(currentPage,countPage)
//            if (listNewOrder.size!=0){
//                mViewDataBinding.constraintNoOrder.visibility=View.GONE
//            }
//            else {
//                mViewDataBinding.constraintNoOrder.visibility=View.VISIBLE
//            }
        }

    }


    override fun acceptedOrRejectedOffer(model: AcceptedOrRejectedOfferSocketResponse) {
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

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            if (view != null) {
                initResponse()
                Utilities.dismissDialogNoInternet()
            }
        } else {
            Utilities.showDialogNoInternet(requireActivity())
        }
    }


}