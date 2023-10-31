package com.sa.nafhasehaprovider.common.util

import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.LinearInterpolator
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import com.sa.nafhasehaprovider.R

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

/**
 * < Vampire >
 */

object MapUtil {


    fun drawPath(result: String, context: Context, googleMap: GoogleMap) {
        try {
            val json = JSONObject(result)
            val routeArray = json.getJSONArray("routes")
            val routes = routeArray.getJSONObject(0)
            val overviewPolylines = routes.getJSONObject("overview_polyline")
            val encodedString = overviewPolylines.getString("points")

            val legs_arr = routes.getJSONArray("legs")
            val legs_obj = legs_arr.getJSONObject(0)
            val distance = legs_obj.getJSONObject("distance")
            val duration = legs_obj.getJSONObject("duration")


            val mDistance = distance.optString("text")
            val mDuration = duration.optString("text")

            val list = decodePoly(encodedString)

            for (z in 0 until list.size - 1) {
                val src = list[z]
                val dest = list[z + 1]
                googleMap.addPolyline(PolylineOptions().add(LatLng(src.latitude, src.longitude),
                        LatLng(dest.latitude, dest.longitude)).width(20f).color(context.resources.getColor(
                    R.color.appColor)).geodesic(true))
            }

        } catch (e: JSONException) {

        }

    }

    private fun decodePoly(encoded: String): List<LatLng> {

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
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    fun animateMarker(googleMap: GoogleMap, marker: Marker, toPosition: LatLng,
                      hideMarker: Boolean) {
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = googleMap.projection
        val startPoint = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 1500

        val interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude
                marker.position = LatLng(lat, lng)
                //                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,
                //                        lng), 14));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 48)
                } else {
                    if (hideMarker) {
                        marker.isVisible = false
                    } else {
                        marker.isVisible = true
                    }
                }
            }
        })
    }
}
