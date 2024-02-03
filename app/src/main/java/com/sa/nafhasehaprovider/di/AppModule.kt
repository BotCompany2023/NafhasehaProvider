package com.sa.nafhasehaprovider.di

import AuthenticationInterceptor
import ConnectivityInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.BASE_URL
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.network.APIEndPoint
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    single(named("base_url")) { BASE_URL }
    single { getRetrofit(get(named("base_url"))) }
    single { createApi(get()) }
    single { createClient() }
    single { PreferencesUtils(androidContext()) }
}

private val moshi by lazy {
    val moshiBuilder = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    moshiBuilder.build()

}

private val loggingInterceptor by lazy {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    loggingInterceptor
}

private fun getRetrofit(baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(createClient()) // Using Koin to get OkHttpClient
        .build()
}

fun createApi(retrofit: Retrofit): APIEndPoint {
    return retrofit.create(APIEndPoint::class.java)
}

fun createClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AuthenticationInterceptor(NafhasehaProviderApp.context!!))
        .addInterceptor(ConnectivityInterceptor(NafhasehaProviderApp.context!!))
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}
