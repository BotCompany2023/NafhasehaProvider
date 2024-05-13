package com.sa.nafhasehaprovider.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.sa.nafhasehaprovider.common.USER_DATA
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils

import com.sa.nafhasehaprovider.di.*
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NafhasehaProviderApp : Application() {

    val sharedPreference: PreferencesUtils by lazy {
        PreferencesUtils(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        lateinit var pref: PreferencesUtils
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        pref = PreferencesUtils(this)


        try {
            if (pref.loadUserData(context!!, USER_DATA)!!.data!!.provider!!.id !=0) {
                SocketRepository.ConnectToSocket()

            }
        }catch (e:Exception)
        {}


        startKoin {
            Log.i("StartKoin", "startkoin")
            androidContext(this@NafhasehaProviderApp)
            modules(
                listOf(
                    repoModule,
                    sharedPreferencesModule,
                    appModule,
                    authenticationViewModelModule,
                    cityViewModelModule,
                    areasViewModelModule,
                    logoutViewModelModule,
                    infoViewModelModule,
                    faqsViewModelModule,
                    notificationViewModelModule,
                    homeViewModelModule,
                    packageModelModule,
                    walletModelModule,
                    myCarModelModule,
                    ordersViewModel,
                )
            )
        }




    }

    override fun onTerminate() {
        super.onTerminate()
        SocketRepository.onDisconnect()
    }

}