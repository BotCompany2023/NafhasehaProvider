package com.sa.nafhasehaprovider.ui.fragment.main.orders

import android.media.Image
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.ImageOrderDetailsAdapter
import com.sa.nafhasehaprovider.adapter.PositionOrderDetailsAdapter
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentShowOrderBinding
import com.sa.nafhasehaprovider.di.ordersViewModel
import com.sa.nafhasehaprovider.entity.request.acceptOrder.AcceptOrderRequest
import com.sa.nafhasehaprovider.entity.response.showOrderResponse.Images
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.fragment.main.home.HomeFragmentDirections
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShowOrderFragment : BaseFragment<FragmentShowOrderBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_show_order

    private var priceType: Int?=null
    private var distance: String? =null
    private var estimatedTime: String? =null
    private var orderLong: String? =null
    private var orderLat: String?=null
    private var userPhone: String?=null
    private var userImage: String? = ""
    private var userName: String? = null
    private var avgRate: String? = null
    private var priceOffer: String? = null
    private var userID: Int?=null
    private var idOrder: Int = 0
    private val viewModel: OrdersViewModel by viewModel()
    private var mainActivity: MainActivity? = null


    lateinit var imageOrderDetailsAdapter: ImageOrderDetailsAdapter
    lateinit var listImage: ArrayList<Images>

    lateinit var positionOrderDetailsAdapter: PositionOrderDetailsAdapter
    lateinit var listPosition: ArrayList<String>


    private lateinit var googleMap: GoogleMap
    private val REQUEST_CHECK_SETTINGS = 10001
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mViewDataBinding.viewBack.visibility=View.VISIBLE

        mainActivity = requireActivity() as MainActivity
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

        onClick()

        if (arguments != null) {
            val args: ShowOrderFragmentArgs = ShowOrderFragmentArgs.fromBundle(requireArguments())
            viewModel.showOrder(args.idOrder)
            idOrder = args.idOrder
        }
        else{
            val receivedBundle = requireActivity().intent.extras
            if (receivedBundle != null) {
                val value = receivedBundle.getString("typeId")
                // استخدم القيمة كما تحتاج
                viewModel.showOrder(value!!.toInt())
            }
        }




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



        //response show orders
        viewModel.showOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                if (it.data!!.user!!.image != null){
                                    userImage=it.data!!.user!!.image
                                }
                                userID=it.data!!.user!!.id
                                userName=it.data.user!!.name
                                userPhone=it.data.user!!.phone
                                orderLat=it.data!!.lat
                                orderLong=it.data!!.long
                                estimatedTime=it.data!!.estimated_time
                                distance=it.data!!.distance
                                priceType=it.data!!.price_type

                                if (it.data!!.is_price_request==1){
                                    priceOffer=""+it.data!!.price_request!!
                                    mViewDataBinding.constraintOfferPrice.visibility=View.VISIBLE
                                    mViewDataBinding.tvOfferPrice.text=""+it.data!!.price_request+" "+ getString(R.string.sar)
                                    mViewDataBinding.btnOffer.text=getString(R.string.update_offer)
                                }
                                else{
                                    priceOffer=it.data!!.suggested_price!!
                                    mViewDataBinding.constraintOfferPrice.visibility=View.GONE
                                    mViewDataBinding.btnOffer.text=getString(R.string.offer)

                                }
                                //Maintenance >>صيانة
                                //PeriodicInspection >> الفحص الدوري
                                //VehicleBarrier >>حواجز سيارات
                                //TransportVehicle >>سطحة
                                //s >>استشاره الاعطال

                                if (it.data!!.type == "TransportVehicle" && it.data.is_offer_price == 0) {
                                    mViewDataBinding.constraintDeliveryLocation.visibility =
                                        View.VISIBLE
                                    mViewDataBinding.btnOffer.visibility = View.GONE
                                    mViewDataBinding.btnAcceptOrder.visibility = View.VISIBLE
                                    mViewDataBinding.constraintDataCar.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE

                                    if (it.data.vehicle_transporter!=null){
                                        Utilities.onLoadImageFromUrl(
                                            requireActivity(),
                                            it.data.vehicle_transporter!!.image!!,
                                            mViewDataBinding.ivLogoCar
                                        )

                                        mViewDataBinding.tvNameCar.text =
                                            it.data.vehicle_transporter.title!!
                                    }



                                }
                                else if (it.data!!.type == "TransportVehicle") {
                                    mViewDataBinding.constraintDeliveryLocation.visibility =
                                        View.VISIBLE
                                    mViewDataBinding.constraintDataCar.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE

                                    if (it.data.vehicle_transporter!=null){
                                        Utilities.onLoadImageFromUrl(
                                            requireActivity(),
                                            it.data.vehicle_transporter!!.image!!,
                                            mViewDataBinding.ivLogoCar
                                        )

                                        mViewDataBinding.tvNameCar.text =
                                            it.data.vehicle_transporter.title!!
                                    }



                                }
                                else if (it.data.type == "PeriodicInspection") {
                                    mViewDataBinding.constraintDataCar.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.VISIBLE
                                    mViewDataBinding.tvModelCar.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE
                                    mViewDataBinding.tvAddress.text = it.data.user!!.address
                                    mViewDataBinding.tvCategoryCar.visibility=View.VISIBLE
                                    mViewDataBinding.btnOffer.visibility=View.GONE
                                    mViewDataBinding.btnAcceptOrder.visibility=View.VISIBLE


                                    Utilities.onLoadImageFromUrl(
                                        requireActivity(),
                                        it.data.user_vehicle!!.first_image!!,
                                        mViewDataBinding.ivLogoCar)
                                    mViewDataBinding.tvNameCar.text =
                                        it.data.user_vehicle!!.vehicle_type!!.title
                                    mViewDataBinding.tvCategoryCar.text =
                                        it.data.user_vehicle!!.vehicle_model!!.vehicle_brand!!.title
                                    mViewDataBinding.tvModelCar.text =
                                        it.data.user_vehicle!!.vehicle_model!!.title

                                }
                                else if (it.data.type == "Maintenance") {
                                    mViewDataBinding.constraintDataCar.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.VISIBLE
                                    mViewDataBinding.tvModelCar.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE
                                    mViewDataBinding.tvAddress.text = it.data.user!!.address
                                    mViewDataBinding.tvCategoryCar.visibility=View.VISIBLE
                                    mViewDataBinding.btnOffer.visibility=View.GONE
                                    mViewDataBinding.btnAcceptOrder.visibility=View.VISIBLE

                                    Utilities.onLoadImageFromUrl(
                                        requireActivity(),
                                        it.data.user_vehicle!!.first_image!!,
                                        mViewDataBinding.ivLogoCar)
                                    mViewDataBinding.tvNameCar.text =
                                        it.data.user_vehicle!!.vehicle_type!!.title
                                    mViewDataBinding.tvCategoryCar.text =
                                        it.data.user_vehicle!!.vehicle_model!!.vehicle_brand!!.title
                                    mViewDataBinding.tvModelCar.text =
                                        it.data.user_vehicle!!.vehicle_model!!.title

                                    listImage = ArrayList()
                                    imageOrderDetailsAdapter =
                                        ImageOrderDetailsAdapter(requireActivity(), listImage)
                                    listImage.addAll(it.data.images!!)
                                    mViewDataBinding.rvHolidayPhotos.adapter = imageOrderDetailsAdapter
                                    imageOrderDetailsAdapter.notifyDataSetChanged()

                                    if (listImage.size !=0)
                                    {
                                        mViewDataBinding.constraintHolidayPhotos.visibility = View.VISIBLE

                                    }
                                }
                                else if (it.data.type == "Petrol") {
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE
                                    mViewDataBinding.constraintPetrol.visibility=View.VISIBLE
                                    mViewDataBinding.tvTypePetrol.text=it.data.type_gasoline
                                    mViewDataBinding.tvAmount.text=""+ it.data!!.price_type+ getString(R.string.sr)

                                }
                                else if (it.data.type == "ChangeBattery") {
                                    mViewDataBinding.constraintTypeBattery.visibility = View.VISIBLE
                                    mViewDataBinding.tvTypeBattery.text =
                                        it.data.type_battery!!.title
                                    mViewDataBinding.tvNameCategory.visibility=View.GONE
                                }
                                else if (it.data.type == "SubscriptionBattery") {
                                    mViewDataBinding.tvNameCategory.text =
                                        "( " + getString(R.string.battery_subscription)+ " )"
                                }

                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.user!!.image,
                                    mViewDataBinding.ivUserImage
                                )
                                mViewDataBinding.tvNameUser.text = it.data!!.user!!.name
                                mViewDataBinding.tvAddress.text = it.data!!.address
                                mViewDataBinding.tvDeliveryLocation.text = it.data!!.address_to

                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.category!!.image,
                                    mViewDataBinding.ivLogoService)

                                mViewDataBinding.tvNameService.text = it.data!!.category!!.title
                                mViewDataBinding.tvNameCategory.text =
                                    "( " + it.data!!.sub_category+ " )"
                                mViewDataBinding.tvOrderCode.text =
                                    getString(R.string.code) + " : " + it.data!!.invoice_no
                                mViewDataBinding.tvComments.text = it.data!!.details

                                if (it.data!!.payment_method !=null){
                                    mViewDataBinding.titlePaymentStatus.visibility =View.VISIBLE
                                    mViewDataBinding.tvPaymentStatus.visibility =View.VISIBLE
                                    mViewDataBinding.tvPaymentStatus.text = it.data!!.payment_method

                                }

                                if (it.data!!.details == null)
                                {
                                    mViewDataBinding.tvComments.visibility=View.GONE
                                    mViewDataBinding.titleComments.visibility=View.GONE
                                }
                                //pending
                                //approved
                                //completed
                                //canceled

                                 if (it.data.status == "pending" && it.data.type == "PeriodicInspection") {
                                     mViewDataBinding.linearAction.visibility = View.VISIBLE

                                     mViewDataBinding.tvTotelPrice.text =
                                         it.data!!.final_total + getString(R.string.sar)
                                     mViewDataBinding.titleExpectedPrice.text =
                                         getString(R.string.total_price)

                                     mViewDataBinding.tvStatus.text = getString(R.string.news)


                                 }

                                 else if (it.data.status == "pending" && it.data.type == "Maintenance") {
                                        mViewDataBinding.linearAction.visibility=View.GONE
                                        mViewDataBinding.constraintExpectedPrice.visibility=View.GONE
                                     mViewDataBinding.btnOffer.visibility=View.GONE
                                     mViewDataBinding.btnAcceptOrder.visibility=View.VISIBLE
                                     mViewDataBinding.tvTotelPrice.text =
                                            it.data!!.suggested_price + getString(R.string.sar)
                                        mViewDataBinding.titleExpectedPrice.text =getString(R.string.starting_price)
                                     mViewDataBinding.tvStatus.text = getString(R.string.news)


                                 }

                                 else if (it.data.status == "pending") {
                                     mViewDataBinding.linearAction.visibility=View.VISIBLE
                                     mViewDataBinding.tvTotelPrice.text =
                                         it.data!!.suggested_price + getString(R.string.sar)
                                     mViewDataBinding.titleExpectedPrice.text =getString(R.string.starting_price)
                                     mViewDataBinding.tvStatus.text = getString(R.string.news)


                                 }


                                 else if (it.data.status == "approved") {
                                     mViewDataBinding.cvMap.visibility=View.VISIBLE
                                     mViewDataBinding.btnFinishOrder.visibility=View.VISIBLE
                                     mViewDataBinding.btnReject.visibility=View.VISIBLE
                                     mViewDataBinding.linearAction.visibility=View.GONE
                                     mViewDataBinding.tvTotelPrice.text =
                                         it.data!!.final_total + getString(R.string.sar)
                                     mViewDataBinding.titleExpectedPrice.text =getString(R.string.total_price)
                                     mViewDataBinding.btnAcceptOrder.visibility=View.GONE
                                     mViewDataBinding.tvStatus.text = getString(R.string.approved)

                                     if (it.data.type == "Maintenance")
                                     {
                                         mViewDataBinding.constraintStatusMessage.visibility=View.VISIBLE
                                         mViewDataBinding.constraintExpectedPrice.visibility=View.GONE
                                         mViewDataBinding.cvMap.visibility=View.GONE
                                         mViewDataBinding.btnFinishOrder.visibility=View.GONE
                                         mViewDataBinding.btnReject.visibility=View.GONE
                                         mViewDataBinding.linearAction.visibility=View.GONE
                                         mViewDataBinding.tvTotelPrice.text =
                                             it.data!!.final_total + getString(R.string.sar)
                                         mViewDataBinding.titleExpectedPrice.text =getString(R.string.total_price)
                                         mViewDataBinding.btnAcceptOrder.visibility=View.GONE
                                         mViewDataBinding.tvStatus.text = getString(R.string.approved)

                                     }
                                     else  if (it.data.type == "TransportVehicle")
                                     {
                                         mViewDataBinding.btnFinishOrder.visibility=View.GONE
                                         mViewDataBinding.btnTheCarMet.visibility=View.VISIBLE
                                         mViewDataBinding.btnReject.visibility=View.VISIBLE
                                         mViewDataBinding.tvStatus.text = getString(R.string.the_client_is_waiting)

                                     }

                                 }
                                 else if (it.data.status == "completed") {
                                    mViewDataBinding.btnFinishOrder.visibility=View.GONE
                                    mViewDataBinding.linearAction.visibility=View.GONE
                                     mViewDataBinding.btnReject.visibility=View.GONE
                                     mViewDataBinding.cvMap.visibility=View.GONE
                                     mViewDataBinding.tvTotelPrice.text =
                                        it.data!!.final_total + getString(R.string.sar)
                                    mViewDataBinding.titleExpectedPrice.text =getString(R.string.total_price)
                                     mViewDataBinding.btnAcceptOrder.visibility=View.GONE
                                     mViewDataBinding.tvStatus.text = getString(R.string.completed)


                                 }
                                else if (it.data.status == "canceled") {
                                     mViewDataBinding.btnReject.visibility=View.GONE
                                     mViewDataBinding.cvMap.visibility=View.GONE
                                     mViewDataBinding.btnFinishOrder.visibility=View.GONE
                                    mViewDataBinding.linearAction.visibility=View.GONE
                                     mViewDataBinding.btnAcceptOrder.visibility=View.GONE
                                     mViewDataBinding.constraintReason.visibility=View.VISIBLE
                                     mViewDataBinding.tvStatus.text = getString(R.string.canceled)
                                     mViewDataBinding.tvStatus.setTextColor(resources.getColor(R.color.red))
                                     mViewDataBinding.tvStatus.setBackgroundResource(R.drawable.shape_cancel)

                                     mViewDataBinding.tvReason.text=it.data.reason

                                 }

                                 else if (it.data.status == "PickUp") {

                                     if (it.data.type == "Maintenance")
                                     {
                                         mViewDataBinding.btnSubmitReports.visibility=View.VISIBLE
                                         mViewDataBinding.constraintExpectedPrice.visibility=View.GONE

                                         if (it.data.is_report==true)
                                         {
                                             mViewDataBinding.tvStatus.setBackgroundResource(R.drawable.shape_pending)
                                             mViewDataBinding.tvStatus.text = getString(R.string.waiting_for_customer_approval)
                                             mViewDataBinding.btnSubmitReports.visibility=View.GONE
                                             mViewDataBinding.btnOffer.visibility=View.GONE
                                             mViewDataBinding.btnAcceptOrder.visibility=View.GONE

                                             if (it.data.report!!.status=="Accept")
                                             {
                                                 mViewDataBinding.btnFinishOrder.visibility=View.VISIBLE
                                                 mViewDataBinding.tvStatus.text =getString(R.string.under_maintenance)

                                                 mViewDataBinding.constraintExpectedPrice.visibility=View.VISIBLE
                                                 mViewDataBinding.tvTotelPrice.text =
                                                     it.data!!.report!!.price + getString(R.string.sar)
                                                 mViewDataBinding.titleExpectedPrice.text =getString(R.string.cost_of_maintenance) +" : "
                                             }
                                         }
                                     }
                                     else{
                                         mViewDataBinding.tvStatus.text = getString(R.string.the_car_is_being_moved)
                                         mViewDataBinding.cvMap.visibility=View.VISIBLE
                                         mViewDataBinding.btnFinishOrder.visibility=View.VISIBLE
                                         mViewDataBinding.btnTheCarMet.visibility=View.GONE
                                         mViewDataBinding.tvTotelPrice.text =
                                             it.data!!.final_total!! + getString(R.string.sar)
                                         mViewDataBinding.titleExpectedPrice.text =getString(R.string.service_cost) +" : "
                                     }

                                 }

                                mViewDataBinding.viewBack.visibility=View.GONE


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
        viewModel.acceptedOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 ->
                            {
                                viewModel.showOrder(idOrder)
                               val model= AcceptOrderRequest(idOrder)
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



        //response storeCompletedOrderResponse
        viewModel.storeCompletedOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 ->
                            {
                                viewModel.showOrder(idOrder)
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



        //response pickUpResponse
        viewModel.pickUpResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 ->
                            {
                                viewModel.showOrder(idOrder)
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


    mViewDataBinding.ivBack.setOnClickListener {
        //mainActivity!!.navController!!.popBackStack()
        val action =
            ShowOrderFragmentDirections.actionShowOrderFragmentToMenuHome()
        mViewDataBinding.root.findNavController().navigate(action)
    }


    mViewDataBinding.btnOffer.setOnClickListener {
        avgRate=NafhasehaProviderApp.pref.getString(PROVIDER_RATING, "")
        val action =
            ShowOrderFragmentDirections.actionShowOrderFragmentToBottomSheetAddOfferFragment(
                idOrder,avgRate!!,priceOffer!!,"SHOW_ORDER_PAGE",priceType!!)
        mViewDataBinding.root.findNavController().navigate(action)
    }


    mViewDataBinding.btnReject.setOnClickListener {
        val action = ShowOrderFragmentDirections
            .actionShowOrderFragmentToBottomSheetDeleteOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)

    }
    mViewDataBinding.tvTraking.setOnClickListener {
        val action = ShowOrderFragmentDirections.
        actionShowOrderFragmentToTrackingMapsFragment(userID!!,
            orderLat!!.toFloat(),
            orderLong!!.toFloat(),
            userImage!!,
            userName!!,
            userPhone!!,
        distance!!,estimatedTime!!,idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }

    mViewDataBinding.btnAcceptOrder.setOnClickListener {
     viewModel.acceptedOrder(idOrder)
    }

    mViewDataBinding.btnFinishOrder.setOnClickListener {
     viewModel.storeCompletedOrder(idOrder)
    }

    mViewDataBinding.btnSubmitReports.setOnClickListener {
        val action = ShowOrderFragmentDirections
            .actionShowOrderFragmentToBottomSheetSubmitReportsFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)

    }

    mViewDataBinding.btnTheCarMet.setOnClickListener {
        viewModel.pickUp(idOrder)
    }

}


override fun onDestroy() {
    super.onDestroy()
    mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
    mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
}


    override fun onResume() {
        super.onResume()
        initResponse()
    }


}