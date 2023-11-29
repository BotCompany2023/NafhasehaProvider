package com.sa.nafhasehaprovider.ui.fragment.main.orders

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.core.utilities.Utilities
import com.google.gson.Gson
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.common.LocationManger.GeoService
import com.sa.nafhasehaprovider.common.onConvertObjToJson
import com.sa.nafhasehaprovider.common.onPrintLog
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onLoadImageFromUrl
import com.sa.nafhasehaprovider.databinding.FragmentTrackingMapsBinding
import com.sa.nafhasehaprovider.entity.response.sockeEmmitModel.TrackerLocation
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository.marker
import com.sa.nafhasehaprovider.ui.activity.MainActivity
import kotlinx.android.synthetic.main.bottom_sheet_behavior.view.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.fragment_show_order.*
import kotlinx.android.synthetic.main.fragment_tracking_maps.*
import okhttp3.OkHttpClient
import okhttp3.Request


class TrackingMapsFragment : Fragment(), OnMapReadyCallback, LocationListener {

    lateinit var  binding:FragmentTrackingMapsBinding
    private lateinit var apiKey: String
    private lateinit var latLngProvider: LatLng
    private var updateLocation: Location? = null
    private lateinit var latLngUser: LatLng
    private lateinit var locationListener: LocationListener
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheetLayout: LinearLayout

    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private val REQUEST_CHECK_SETTINGS = 10001
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private var mainActivity: MainActivity? = null
    private val PLAY_SERVICE_RESULATION_REQUEST = 300193
    private var geoService: GeoService? = null

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var isMarkerMoving: Boolean = false

    private var orderLong: Float? = null
    private var orderLat: Float? = null
    private var userPhone: String? = null
    private var userImage: String? = null
    private var userName: String? = null
    private var orderID: Int? = null
    private var userID: Int? = null
    private var distance: String? =null
    private var estimatedTime: String? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_tracking_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = requireActivity().packageManager
            .getApplicationInfo(requireActivity().packageName, PackageManager.GET_META_DATA)
//        val value = ai.metaData["YOUR_API_KEY"]
//        val value = ai.metaData["com.google.android.geo.API_KEY"]
        apiKey = "AIzaSyA0Z9_o0V_KIayrf5ViLm6qf67IwwfRyuk"
        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity(), apiKey)
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_tracking) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        mainActivity = requireActivity() as MainActivity
        mainActivity!!.mViewDataBinding.bottomNav.visibility = View.GONE
        mainActivity!!.mViewDataBinding.toolbar.visibility = View.GONE

        bottomSheetLayout = view.findViewById<LinearLayout>(R.id.bottomSheetLayout)

        if (arguments != null) {
            val args: TrackingMapsFragmentArgs =
                TrackingMapsFragmentArgs.fromBundle(requireArguments())
            orderID = args.idOrder
            userID = args.userID
            userName = args.userName
            userImage = args.userImage
            userPhone = args.userPhone
            orderLat = args.userLat
            orderLong = args.userLong
            distance = args.distance
            estimatedTime = args.estimatedTime

            bottomSheetLayout.tv_time.text=estimatedTime!!
            bottomSheetLayout.tv_distance.text=distance!! +getString(R.string.km)
            bottomSheetLayout.tv_name_user.text=userName!!
            onLoadImageFromUrl(
                requireActivity(), userImage, bottomSheetLayout.iv_client
            )

            bottomSheetLayout.tv_cancel_order.setOnClickListener {
                val action = TrackingMapsFragmentDirections
                    .actionTrackingMapsFragmentToBottomSheetDeleteOrderFragment(orderID!!)
                mainActivity!!.navController.navigate(action)
            }

        }

        //Initialize fused location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

        bottomSheetBehavior!!.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetLayout.visibility =
//                        View.GONE
//
//                } else {
//                    bottomSheet.visibility =
//                        View.VISIBLE
//                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("dddd", "onSlide: slideOffset$slideOffset")
//                if(startTripBtnDone){

            }
        })

        getLocation()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        latLngUser = LatLng(orderLat!!.toDouble(), orderLong!!.toDouble())
        // إضافة المركر الثابت
        googleMap.addMarker(
            MarkerOptions()
                .position(latLngUser)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marker))
        )
        //zoom map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 17f))




    }

    private fun getLocation() {
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
    }

    override fun onLocationChanged(location: Location) {

        mMap.clear()

        // إحداثيات المركر المتحرك
        latLngProvider = LatLng(location!!.latitude, location!!.longitude)

        // إضافة المركر المتحرك
        val marker2 = mMap.addMarker(
            MarkerOptions()
                .position(latLngProvider)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markar_provider))
        )
       mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngProvider, 17f))


        // إضافة المركر الثابت
        val marker1=mMap.addMarker(
            MarkerOptions()
                .position(latLngUser)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marker))
        )




//        // تحديث موقع المركر المتحرك
//        val handler = Handler()
//        val runnable = object : Runnable {
//            override fun run() {
//                val randomLatLng = LatLng(
//                    marker2!!.position.latitude + (Math.random() - 0.5) / 10,
//                    marker2.position.longitude + (Math.random() - 0.5) / 10
//                )
//                marker2.position = randomLatLng
//                handler.postDelayed(this, 1000)
//            }
//        }
//        handler.post(runnable)



//
//        // Initialize lat lng
//        val latLngProvider = LatLng(location!!.latitude, location!!.longitude)
//        //create Marker option
//        val options = MarkerOptions().position(latLngProvider)
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markar_provider))
//        //zoom map
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngProvider, 17f))
//        //add marker on map
//        mMap.addMarker(options)
//
//
//        // Initialize lat lng
//        latLngUser = LatLng(orderLat!!.toDouble(), orderLong!!.toDouble())
//        //create Marker option
//        val optionsUser = MarkerOptions().position(latLngUser)
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marker))
//        //zoom map
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngUser, 17f))
//        //add marker on map
//        mMap.addMarker(optionsUser)
//
//        val builder = LatLngBounds.Builder()
//        builder.include(latLngProvider)
//        builder.include(latLngUser)
//
//        val bounds = builder.build()
//        val padding = 200
//        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
//        mMap.moveCamera(cameraUpdate)


//        val builder = LatLngBounds.Builder()
//        builder.include(marker1!!.position)
//        builder.include(marker2!!.position)
//        val bounds = builder.build()
//        val padding = 100 // padding بيكون بالبكسل
//        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
//        mMap.animateCamera(cu)



        val location1 = TrackerLocation(
            userID!!,
            location.latitude,
            location.longitude)
        onPrintLog(onConvertObjToJson(location1))

        SocketRepository.onSendLocation(location1)


        val urll = getDirectionURL(latLngUser, latLngProvider, apiKey)
      // GetDirection(urll).execute()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data,MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(15f)
                lineoption.color(R.color.appColor)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }


}