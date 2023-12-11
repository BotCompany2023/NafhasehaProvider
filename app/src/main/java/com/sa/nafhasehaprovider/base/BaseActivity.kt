package com.sa.nafhasehaprovider.base

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.common.LANG
import com.sa.nafhasehaprovider.common.util.LocaleUtil

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewDataBinding: T
    protected lateinit var activity: AppCompatActivity
    private lateinit var progressDialog: Dialog
    private lateinit var dialogNoInternet: Dialog


    protected val sharedPreference: PreferencesUtils by lazy {
        (application as NafhasehaProviderApp).sharedPreference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        activity = this
        performDataBinding()
        // makeStatusbarTransparent()

        val connectivityManager = getSystemService(
            ConnectivityManager::class.java
        ) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

        checkConnection()
    }


    var networkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()!!

    private val networkCallback: NetworkCallback = object : NetworkCallback() {
        override fun onAvailable(@NonNull network: Network) {
            super.onAvailable(network)
            dismissDialogNoInternet()
        }

        override fun onLost(@NonNull network: Network) {
            super.onLost(network)
            showDialogNoInternet()
        }

        override fun onCapabilitiesChanged(
            @NonNull network: Network, @NonNull networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }
    }


    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        startActivity(mainIntent)
    }

    fun openActivityAndFinish(destinationActivity: Class<*>, bundle: Bundle? = null) {
        val mainIntent = Intent(activity, destinationActivity)
        bundle?.let { mainIntent.putExtras(it) }
        startActivity(mainIntent)
        finish()
    }

    override fun attachBaseContext(newBase: Context) {
        var prefrence = PreferencesUtils(newBase)
        applyOverrideConfiguration(
            LocaleUtil.getLocalizedConfiguration(
                prefrence.getString(
                    LANG, "ar"
                )!!
            )
        )
        super.attachBaseContext(newBase)
    }

    fun showProgress(it: Boolean) {
        if (it) showProgressDialog()
        else hideProgressDialog()

    }

    fun showProgressDialog() {
        hideProgressDialog()
        val alertDialogBuilder = AlertDialog.Builder(this).setCancelable(false)
        alertDialogBuilder.setView(R.layout.loader)
        progressDialog = alertDialogBuilder.create()
        progressDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) progressDialog.dismiss()
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /*   fun setLocal(lang: String, activity: Activity) {
           Utilities.cacheString(activity, LANG, lang)
           Utilities.setLocal(lang, activity)
       }
   */

    /*   fun showToast() {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.full_color_toast, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = getString(R.string.data_used_in_other_phone)
           startPulseAnimation(this, layout = layout)
   
           // init toast
           val toast = Toast(applicationContext)
           startTimer(20000L, toast)
   
           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)
   
           // Setting layout to toast
           toast.view = layout
           toast.show()
       }
   
   
       fun warningToast( message: String) {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.warning_toast, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = message
           startPulseAnimation(this, layout = layout)
   
           // init toast
           val toast = Toast(applicationContext)
           startTimer(20000L, toast)
   
           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)
   
           // Setting layout to toast
           toast.view = layout
           toast.show()
       }
   
   
       fun errorToast(message: String) {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.full_color_toast, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = message
           startPulseAnimation(this, layout = layout)
           // init toast
           val toast = Toast(applicationContext)
   //        startTimer(20000L, toast)
           toast.duration = Toast.LENGTH_LONG
           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)
   
           // Setting layout to toast
           toast.view = layout
           toast.show()
       }
   
   
       fun successToast(message: String) {
           var layoutInflater = LayoutInflater.from(this)
           val layout = layoutInflater.inflate(
               R.layout.full_color_toast2, findViewById(R.id.color_toast_view)
           )
   //        layout.color_toast_text.text = title
           layout.color_toast_description.text = message
           startPulseAnimation(this, layout = layout)
           // init toast
           val toast = Toast(applicationContext)
           startTimer(20000L, toast)
   
           // Setting Toast Gravity
           toast.setGravity(60, 50, 250)
   
           // Setting layout to toast
           toast.view = layout
           toast.show()
       }
   */
    override fun onResume() {
        super.onResume()
        // Utilities.setLocalFrom(Utilities.getCachedString(this, LANG, "ar"), this)
    }

    fun setLocal(lang: String, activity: Activity) {
//        Utilities.cacheString(activity, LANG, lang)
//        Utilities.setLocal(lang, activity)
    }




    private fun makeStatusbarTransparent() {
        window.navigationBarColor = resources.getColor(R.color.back_layout)
        window.statusBarColor = resources.getColor(R.color.back_layout)

    }

    private fun setupWindowAnimations() {
        val fade: Transition? =
            TransitionInflater.from(this).inflateTransition(R.transition.activity_fade)
        window.enterTransition = fade
    }


    open fun checkConnection() {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val network = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifi!!.isConnected) {
            //Internet available
            dismissDialogNoInternet()
        } else if (network!!.isConnected) {
            //Internet available
            dismissDialogNoInternet()
        } else {
            //Internet is not available
            showDialogNoInternet()
        }
    }

    open fun showDialogNoInternet() {
        dialogNoInternet = Dialog(this, R.style.customDialogTheme)
        dialogNoInternet.setCancelable(false)
        val inflater = this.layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_check_internet, null)
        dialogNoInternet.setContentView(v)
        val window = dialogNoInternet.getWindow()
        window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        try {
            dialogNoInternet.show()
        } catch (e: Exception) {
        }
    }

    open fun dismissDialogNoInternet() {
        try {
            dialogNoInternet!!.dismiss()
        } catch (e: Exception) {
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//            SocketRepository.onDisconnect()
//    }
//    override fun onPause() {
//        super.onPause()
//        SocketRepository.onDisconnect()
//
//    }
}
