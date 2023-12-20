package com.sa.nafhasehaprovider.di

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.BEARER
import com.sa.nafhasehaprovider.common.LANG

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException



class AuthenticationInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            showToast("لا يوجد اتصال بالإنترنت")
            // يمكنك تكملة الكود هنا حسب احتياجات التطبيق
            // يمكنك على سبيل المثال إلغاء الطلب أو تحديد رمز خطأ خاص بفقدان الاتصال
        }

        val originalRequest: Request = chain.request()
        val token = BEARER + " " + NafhasehaProviderApp.pref.authToken!!.toString()
        val lang = NafhasehaProviderApp.pref.getString(LANG, "ar").toString()

        val requestBuilder: Request.Builder = originalRequest.newBuilder()
            .header("Authorization", token)
            .header("Accept-Language", lang)
            .method(originalRequest.method, originalRequest.body)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun showToast(message: String) {
        (context as Activity).runOnUiThread {
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
        }

    }
}

class NoConnectivityException(message: String) : IOException(message)