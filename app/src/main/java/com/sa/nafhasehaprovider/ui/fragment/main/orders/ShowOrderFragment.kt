package com.sa.nafhasehaprovider.ui.fragment.main.orders

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.maps.android.SphericalUtil
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.adapter.ImageOrderDetailsAdapter
import com.sa.nafhasehaprovider.adapter.PositionOrderDetailsAdapter
import com.sa.nafhasehaprovider.base.BaseFragment
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.FragmentShowOrderBinding
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import com.sa.nafhasehaprovider.ui.fragment.auth.login.LoginFragmentDirections
import com.sa.nafhasehaprovider.viewModels.OrdersViewModel
import kotlinx.android.synthetic.main.fragment_show_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShowOrderFragment : BaseFragment<FragmentShowOrderBinding>(), OnMapReadyCallback {

    override fun getLayoutId(): Int = R.layout.fragment_show_order

    private var idOrder: Int=0
    private val viewModel: OrdersViewModel by viewModel()
    private var mainActivity: MainActivity? = null


    lateinit var imageOrderDetailsAdapter: ImageOrderDetailsAdapter
    lateinit var listImage: ArrayList<String>

    lateinit var positionOrderDetailsAdapter: PositionOrderDetailsAdapter
    lateinit var listPosition: ArrayList<String>


    private lateinit var googleMap: GoogleMap
    private val REQUEST_CHECK_SETTINGS = 10001
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

        initResponse()
        onClick()

        if (arguments != null) {
            val args: ShowOrderFragmentArgs = ShowOrderFragmentArgs.fromBundle(requireArguments())
            viewModel.showOrder(args.idOrder)
            idOrder=args.idOrder
        }


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //Initialize fused location
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())

        task.addOnCompleteListener { task1: Task<LocationSettingsResponse?> ->
            try {
                val response = task1.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                requireActivity(), REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
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

                                //Maintenance >>صيانة
                                //PeriodicInspection >> الفحص الدوري
                                //VehicleBarrier >>حواجز سيارات
                                //TransportVehicle >>سطحة
                                //s >>استشاره الاعطال

                                if (it.data!!.type == "TransportVehicle") {
                                    mViewDataBinding.constraintDeliveryLocation.visibility =
                                        View.VISIBLE
                                    mViewDataBinding.constraintSuperficial.visibility = View.VISIBLE
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE

                                } else if (it.data.type == "VehicleBarrier") {
                                    mViewDataBinding.rvPosition.visibility = View.VISIBLE
                                    mViewDataBinding.constraintDeliveryLocation.visibility =
                                        View.GONE
                                    mViewDataBinding.constraintSuperficial.visibility = View.GONE
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE
                                    mViewDataBinding.tvAddress.text = it.data.user.address
                                    mViewDataBinding.view5.visibility = View.GONE

                                } else if (it.data.type == "Maintenance") {
                                    mViewDataBinding.tvNameCategory.visibility = View.VISIBLE
                                    mViewDataBinding.view5.visibility = View.GONE

                                } else if (it.data.type == "PeriodicInspection") {
                                    mViewDataBinding.tvNameCategory.visibility = View.GONE
                                    mViewDataBinding.view5.visibility = View.GONE
                                    mViewDataBinding.tvAddress.text = it.data.user.address

                                } else {
                                    mViewDataBinding.constraintDeliveryLocation.visibility =
                                        View.GONE
                                    mViewDataBinding.constraintSuperficial.visibility = View.GONE
                                }

                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.user.image,
                                    mViewDataBinding.ivUserImage
                                )
                                mViewDataBinding.tvNameUser.text = it.data!!.user.name
                                mViewDataBinding.tvAddress.text = it.data!!.address
                                mViewDataBinding.tvDeliveryLocation.text = it.data!!.address_to
                                mViewDataBinding.tvTime.text = it.data!!.time_at
                                mViewDataBinding.tvDate.text = it.data!!.date_at

                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.service.image,
                                    mViewDataBinding.ivLogoService
                                )
                                mViewDataBinding.tvNameService.text = it.data!!.service.title
                                mViewDataBinding.tvNameCategory.text =
                                    "( " + it.data!!.category.title + " )"
                                mViewDataBinding.tvOrderCode.text =
                                    getString(R.string.code) + " : " + it.data!!.invoice_no
                                mViewDataBinding.tvDescription.text = it.data!!.details
                                mViewDataBinding.tvComments.text = it.data!!.details
                                mViewDataBinding.tvExpectedPrice.text =
                                    it.data!!.final_total + getString(R.string.sar)
                                mViewDataBinding.tvPaymentStatus.text = it.data!!.payment_type

                                try {

                                    Utilities.onLoadImageFromUrl(
                                        requireActivity(),
                                        it.data.vehicle_transporter!!.image!!,
                                        mViewDataBinding.ivLogoSathuh
                                    )
                                    mViewDataBinding.tvSathuh.text =
                                        it.data.vehicle_transporter.title!!


                                    listImage = ArrayList()
                                    imageOrderDetailsAdapter =
                                        ImageOrderDetailsAdapter(requireActivity(), listImage)
                                    listImage.addAll(it.data.images!!)
                                    mViewDataBinding.rvImages.adapter = imageOrderDetailsAdapter
                                    imageOrderDetailsAdapter.notifyDataSetChanged()
                                    if (listImage.size != null) {
                                        mViewDataBinding.view5.visibility = View.VISIBLE

                                    }



                                    listPosition = ArrayList()
                                    positionOrderDetailsAdapter =
                                        PositionOrderDetailsAdapter(requireActivity(), listPosition)
                                    listPosition.addAll(it.data.position!!)
                                    mViewDataBinding.rvPosition.adapter =
                                        positionOrderDetailsAdapter
                                    positionOrderDetailsAdapter.notifyDataSetChanged()

                                } catch (e: Exception) {
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

        mViewDataBinding.btnOffer.setOnClickListener {
            val action = ShowOrderFragmentDirections.actionShowOrderFragmentToBottomSheetAddOfferFragment(idOrder)
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.btnReject.setOnClickListener {
            val action = ShowOrderFragmentDirections.actionShowOrderFragmentToBottomSheetDeleteOrderFragment(idOrder)
            mViewDataBinding.root.findNavController().navigate(action)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.VISIBLE
    }


    override fun onMapReady(map: GoogleMap) {


        map.let {
            googleMap = it
            // Request location permission
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Enable GPS button
                googleMap.uiSettings.isMyLocationButtonEnabled = true
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled
                googleMap.uiSettings.isScrollGesturesEnabled = false
                googleMap.uiSettings.isMyLocationButtonEnabled = true


                // Initialize lat lng
                val startPoint = LatLng(24.80592, 46.78202)
                //create Marker option
                val options = MarkerOptions().position(startPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marker))
                //zoom map
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 9f))
                //add marker on map
                it.addMarker(options)


                // Initialize lat lng
                val endPoint = LatLng(21.37465, 39.71549)
                //create Marker option
                val options1 = MarkerOptions().position(endPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marck))
                //zoom map
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(endPoint, 9f))
                //add marker on map
                it.addMarker(options1)


                val builder = LatLngBounds.Builder()
                builder.include(startPoint)
                builder.include(endPoint)
                val bounds = builder.build()
                val padding = 100
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                googleMap.moveCamera(cameraUpdate)

                // إنشاء PolylineOptions وإضافة النقاط
                val polylineOptions = PolylineOptions()
                    .add(startPoint, endPoint)

                // تعيين خيارات الخطوط
                polylineOptions.color(resources.getColor(R.color.yellow)) // لون الخط
                polylineOptions.width(5f) // عرض الخط
                polylineOptions.geodesic(true) // يجعل الخط يتبع المسار الأقصر على الأرض

                // إضافة Polyline إلى الخريطة
                googleMap.addPolyline(polylineOptions)


            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }


    // حساب النقاط الفرعية على طول الخط المنحني
    fun getCurvePoints(startPoint: LatLng, endPoint: LatLng, intensity: Double): List<LatLng> {
        val curvePoints = mutableListOf<LatLng>()

        val dist = SphericalUtil.computeDistanceBetween(
            startPoint, endPoint
        ) // حساب المسافة بين النقطتين
        val heading =
            SphericalUtil.computeHeading(startPoint, endPoint) // حساب الاتجاه بين النقطتين

        val step = 1 / (dist / 100) // تحديد الخطوة

        // حساب النقاط الفرعية
        for (i in 0..100) {
            val t = step * i
            val lat = (1 - t) * startPoint.latitude + t * endPoint.latitude
            val lng = (1 - t) * startPoint.longitude + t * endPoint.longitude
            val point = LatLng(lat, lng)

            val curveIntensity = Math.sin(t * Math.PI) * intensity

            val curveHeading = heading + 90.0 // تحديد اتجاه الانحناء
            val curvePoint = SphericalUtil.computeOffset(point, curveIntensity, curveHeading)
            curvePoints.add(curvePoint)
        }

        return curvePoints
    }


}