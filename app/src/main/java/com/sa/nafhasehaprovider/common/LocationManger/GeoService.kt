package com.sa.nafhasehaprovider.common.LocationManger


import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.sa.nafhasehaprovider.ui.activity.MainActivity

/**
 * < Vampire >
 */

class GeoService : Service(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    var activity: MainActivity? = null

    private var mLocationChangeListener: LocationChangeListener? = null


    // Is the service tracking time?
    /**
     * @return whether the service is running
     */
    var isServiceRunning: Boolean = false
        private set

    // Service binder
    private val serviceBinder = RunServiceBinder()

    inner class RunServiceBinder : Binder() {
        val service: GeoService
            get() = this@GeoService
    }

    override fun onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Creating service")
        }
        //        ref = FirebaseDatabase.getInstance().getReference("MyLocation");
        //        geoFire = new GeoFire(ref);
        isServiceRunning = false
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting service")
        }
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Binding service")
        }
        return serviceBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Destroying service")
        }
    }

    /**
     * Starts the timer
     */
    fun startService(latLng: LatLng, radius: Double) {
        Log.e(TAG, "startService request for an already running Service" + latLng.latitude)

        if (!isServiceRunning) {
            isServiceRunning = true

        } else {
            Log.e(TAG, "startService request for an already running Service")

        }
        //        if (geoQuery!=null){
        //            geoQuery.removeAllListeners();
        //        }
        //        geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 2f);
        //        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
        //            @Override
        //            public void onKeyEntered(String key, GeoLocation location) {
        //                sendNotification("MRF", String.format("%s entered the dangerous area", key));
        //            }
        //
        //            @Override
        //            public void onKeyExited(String key) {
        //                sendNotification("MRF", String.format("%s exit the dangerous area", key));
        //            }
        //
        //            @Override
        //            public void onKeyMoved(String key, GeoLocation location) {
        //                Log.d("MOVE", String.format("%s move within the dangerous area [%f/%f]", key, location.latitude, location.longitude));
        //            }
        //
        //            @Override
        //            public void onGeoQueryReady() {
        //
        //            }
        //
        //            @Override
        //            public void onGeoQueryError(DatabaseError error) {
        //                Log.d("ERROR", "" + error);
        //            }
        //        });
    }

    /**
     * Stops the timer
     */
    fun stopService() {
        if (isServiceRunning) {
            isServiceRunning = false
        } else {
            Log.e(TAG, "stopTimer request for a timer that isn't running")
        }
    }


    /**
     * Place the service into the foreground
     */
    fun foreground() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    /**
     * Return the service to the background
     */
    fun background() {
        stopForeground(true)
    }

    /**
     * Creates a notification for placing the service into the foreground
     *
     * @return a notification for interacting with the service when in the foreground
     */
    private fun createNotification(): Notification {
        Log.e(">>", "here")
        val builder = NotificationCompat.Builder(this)
            .setContentTitle("Service is Active")
            .setContentText("Tap to return to the Map")
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)

        return builder.build()
    }



    override fun onConnected(bundle: Bundle?) {
        displayLocation()
        startLocationUpdate()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient!!, mLocationRequest!!, this)
        }
        catch (e:Exception){

        }

    }

    override fun onConnectionSuspended(i: Int) {
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        displayLocation()
    }

    interface LocationChangeListener {
        fun onLocationChange(location: Location)
    }

    fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        val UPDATE_INTERVAL = 5000
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        val FATEST_INTERVAL = 3000
        mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val DISPLACEMENT = 10
        mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    @SuppressLint("MissingPermission")
    fun displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient!!)
        if (mLastLocation != null) {
            val latitude = mLastLocation!!.latitude
            val longitude = mLastLocation!!.longitude
            Log.e(TAG, "startService request for an already running Service" + mLastLocation!!.latitude)
            mLocationChangeListener!!.onLocationChange(mLastLocation!!)


            Log.d("MRF", String.format("Your last location was chaged: %f / %f", latitude, longitude))
        } else {
            Log.d("MRF", "Can not get your location.")
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest!!)
            builder.setAlwaysShow(true)
            val result = LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient!!,
                builder.build()
            )

            result.setResultCallback { result ->
                //TODO
                when (result.status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                    }

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        //  Location settings are not satisfied. Show the user a dialog

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().

                            result.status.startResolutionForResult(activity!!, 11)

                        } catch (e: IntentSender.SendIntentException) {

                            //failed to show
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    fun setLocationChangeListener(mLocationChangeListener: LocationChangeListener) {
        this.mLocationChangeListener = mLocationChangeListener
    }

    companion object {
        private val TAG = GeoService::class.java.simpleName

        // Foreground notification id
        private val NOTIFICATION_ID = 1
    }

}