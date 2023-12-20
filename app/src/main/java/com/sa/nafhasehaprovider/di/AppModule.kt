package com.sa.nafhasehaprovider.di

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.BASE_URL
import com.sa.nafhasehaprovider.common.LANG
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.network.APIEndPoint
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    // must create interceptor first
    single { getRetrofit() }
    single { createClient() }
    single { createApi() }
    single { PreferencesUtils(androidContext()) }
}

/////////////////////////////// network module //////////////////


private val moshi by lazy {
    val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory())
    moshiBuilder.build()
}

private val loggingInterceptor by lazy {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    loggingInterceptor
}

private fun getRetrofit(): Retrofit {

    return Retrofit.Builder()
        // add base url as constant
        .baseUrl(BASE_URL)
        //.client(CreateHttp())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(createClient())
        .build()
}

fun createApi(): APIEndPoint {
    val retrofit = getRetrofit()
    return retrofit.create(APIEndPoint::class.java)
}


fun createClient(): OkHttpClient {

    Log.i("TestModule" , " create client function base url  $BASE_URL")
    if (!NafhasehaProviderApp.pref.authToken.isNullOrEmpty()) {
        Log.i("TestHome" , " iff client  token is ${NafhasehaProviderApp.pref.authToken}")

        val authClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(Interceptor { chain ->
//
//                    val request: Request =
//                        chain.request().newBuilder()
//                            .addHeader("Accept-Language", SmartCarbApp.pref.getString(LANG, "ar").toString())
//                            .addHeader("Authorization", BEARER +" "+ SmartCarbApp.pref.authToken!!.toString())
//                            .build()
//                    chain.proceed(request)
//            })
            .addInterceptor(AuthenticationInterceptor(NafhasehaProviderApp.context!!))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        return authClient
    }
    else {
        Log.i("TestHome" , " else client  token is ${NafhasehaProviderApp.pref.authToken}")

//        Log.i("TestHome" , " else ${BASE_URL}")

        val nonAuthClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
//            .addInterceptor(Interceptor { chain ->
//                val request: Request =
//                    chain.request().newBuilder()
//                        .addHeader("Accept-Language", SmartCarbApp.pref.getString(LANG, "ar").toString())
//                        .addHeader("Authorization", BEARER +" "+ SmartCarbApp.pref.authToken!!.toString())
//                        .build()
//                chain.proceed(request)
//            })
            .addInterceptor(AuthenticationInterceptor(NafhasehaProviderApp.context!!))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        return nonAuthClient
    }
}