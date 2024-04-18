package com.sa.nafhasehaprovider.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.common.util.Utilities.Companion.onPermission
import com.sa.nafhasehaprovider.databinding.ActivityMainBinding
import com.sa.nafhasehaprovider.network.soketManager.SocketRepository
import com.sa.nafhasehaprovider.ui.activity.notification.NotificationActivity
import com.sa.nafhasehaprovider.viewModels.HomeViewModel
import com.sa.nafhasehaprovider.viewModels.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    private val idOrder: String =""
    private lateinit var orderStep: String
    private val viewModel: NotificationViewModel by viewModel()


//    lateinit var type: String
    var navController: NavController? =null

    private lateinit var nbody: String
    private lateinit var ntitle: String
    private lateinit var typeId: String
    private lateinit var checkFragment: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusbarTransparent()
        //setupWindowAnimations()
        checkFragment = intent.getStringExtra("type").toString()

        onPermission(this@MainActivity)
        getFirebaseToken()
        initResponse()




        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        navController = navHostFragment.navController
        val navigation: BottomNavigationView = findViewById(R.id.bottomNav)
        val navController = findNavController(
            R.id.nav_host_main
        )
        setupWithNavController(navigation, navController)

        init()

        when {
            checkFragment == "SETTING" -> {
                navController.navigate(R.id.menuSetting)
            }
            //طلب جديد
//            checkFragment == "1" && orderStep=="New" -> {
////                val bundle = Bundle()
////                bundle.putString("typeId",idOrder)
////                navController!!.navigate(R.id.showOrderFragment, bundle)
//                var intent= Intent(this, MainActivity::class.java)
//                startActivity(intent)
//
//            }
//            //قبول عرض السعر
//            checkFragment == "1" && orderStep=="Accept" -> {
////                val bundle = Bundle()
////                bundle.putInt("typeId",idOrder.toInt())
////                navController!!.navigate(R.id.showOrderFragment, bundle)
//
//                var intent= Intent(this, MainActivity::class.java)
//                startActivity(intent)
//
//            }
//            checkFragment == "3" -> {
//                var intent= Intent(this, NotificationActivity::class.java)
//                intent.putExtra("TypeNotification",checkFragment)
//                intent.putExtra("typeId",idOrder)
//                intent.putExtra("title",ntitle)
//                intent.putExtra("body",nbody)
//                startActivity(intent)
//
//            }

        }


        navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    navController.navigate(R.id.menuHome)
//                    mViewDataBinding.ivServiceMap.setImageResource(R.drawable.ic_service_map)
                    mViewDataBinding.toolbar.visibility = View.VISIBLE

                    true
                }
                R.id.menuWallet -> {
                    navController.navigate(R.id.menuWallet)
//                    mViewDataBinding.ivServiceMap.setImageResource(R.drawable.ic_service_map)
                    mViewDataBinding.toolbar.visibility = View.VISIBLE

                    true
                }

                R.id.menuOrders -> {
                    navController.navigate(R.id.menuOrders)
//                    mViewDataBinding.ivServiceMap.setImageResource(R.drawable.ic_service_map)
                    mViewDataBinding.toolbar.visibility = View.VISIBLE

                    true
                }
                R.id.menuSetting -> {
                    navController.navigate(R.id.menuSetting)
//                    mViewDataBinding.ivServiceMap.setImageResource(R.drawable.ic_service_map)
                    mViewDataBinding.toolbar.visibility = View.VISIBLE

                    true
                }

                else -> {
                    true
                }
            }
        }


        mViewDataBinding.ivNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
            Animatoo.animateZoom(this);
        }

        mViewDataBinding.constraintSwitch.setOnClickListener {
            viewModel.changeStatusGetOrders()
            navController.navigate(R.id.menuHome)
        }




    }


    private fun setupWindowAnimations() {
        val slide: Slide =
            TransitionInflater.from(this).inflateTransition(R.transition.activity_slide) as Slide
        window.exitTransition = slide
    }

    private fun makeStatusbarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = resources.getColor(R.color.black)
            window.statusBarColor =resources.getColor(R.color.back_layout)
        } else {
            val window = window
            //window.statusBarColor = Color.TRANSPARENT
            window.statusBarColor =resources.getColor(R.color.back_layout)
            window.navigationBarColor = resources.getColor(R.color.black)

        }

    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TestFireBase", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            val firebaseToken = token
            Log.w("TestFireBase", token)
            viewModel.saveToken(token)
        })
    }


    private fun initResponse() {
        // api response
        viewModel.countNotification()
        viewModel.countNotificationResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {

                            CODE200 -> {
                                COUNT_NOTIFICATION= it.data.count_notification
                                mViewDataBinding.tvCount.text= it.data.count_notification.toString()
                                if (it.data.get_orders==1){
                                    mViewDataBinding.ivOnOff.setImageResource(R.drawable.icon_on)
                                    mViewDataBinding.tvTitleStatus.text=getText(R.string.available)
                                }
                                else{
                                    mViewDataBinding.ivOnOff.setImageResource(R.drawable.icon_off)
                                    mViewDataBinding.tvTitleStatus.text=getText(R.string.uavailable)
                                }
                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.logOutApp(this)
                            }
                            CODE405 -> {
                                Utilities.showToastError(this, it.message)
                                showProgress(false)
                            }
                            CODE500 -> {
                                Utilities.showToastError(this, it.message)
                            }
                            else -> {
                                Utilities.showToastError(this, it.message)
                                showProgress(false)
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
                    showProgress(false)

                }
            }
        })



        // api response
        viewModel.saveTokenResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.showToastSuccess(this, it.message)
                            }
                            CODE405 -> {
                                Utilities.showToastError(this, it.message)
                                showProgress(false)
                            }
                            CODE500 -> {
                                Utilities.showToastError(this, it.message)
                            }
                            else -> {
                                Utilities.showToastError(this, it.message)
                                showProgress(false)
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
                    showProgress(false)

                }
            }
        })


        viewModel.changeStatusGetOrdersResponse.observe(this, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    // dismiss loading
                    showProgress(false)

                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                if (it.data!! ==1){
                                    Utilities.showToastSuccess(this, getString(R.string.available))
                                }
                                else{
                                    Utilities.showToastSuccess(this, getString(R.string.uavailable))

                                }
                                if (it.data ==1){
                                    mViewDataBinding.ivOnOff.setImageResource(R.drawable.icon_on)
                                    mViewDataBinding.tvTitleStatus.text=getText(R.string.available)
                                }
                                else{
                                    mViewDataBinding.ivOnOff.setImageResource(R.drawable.icon_off)
                                    mViewDataBinding.tvTitleStatus.text=getText(R.string.uavailable)
                                }

                            }
                            CODE403 -> {
                                //unAuthorized()
                                Utilities.showToastError(this, it.message)
                                Utilities.logOutApp(this)

                            }
                            CODE405 -> {
                                Utilities.showToastError(this, it.message)

                            }
                            CODE500 -> {
                                Utilities.showToastError(this, it.message)
                            }
                            else -> {
                                Utilities.showToastError(this, it.message)
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

    private fun init() {
        checkFragment = intent.getStringExtra("type").toString()
        typeId = intent.getStringExtra("type_id").toString()
        ntitle = intent.getStringExtra("title").toString()
        nbody = intent.getStringExtra("body").toString()
        orderStep = intent.getStringExtra("order_step").toString()

        //طلب جديد
        if (checkFragment == "1" && orderStep=="New" || orderStep=="Price"
            || orderStep=="Accept"|| orderStep=="Complete") {
//            var intent= Intent(this, MainActivity::class.java)
//            intent.putExtra("type",checkFragment)
//            intent.putExtra("type_id",typeId)
//            intent.putExtra("order_step",orderStep)
//            startActivity(intent)

            navController!!.navigate(R.id.menuHome)
            mViewDataBinding.toolbar.visibility = View.VISIBLE

        }

        else if (checkFragment == "2") {
            var intent= Intent(this, MainActivity::class.java)
            intent.putExtra("type_id",typeId)
            startActivity(intent)
        }
        else if (checkFragment == "3") {
            var intent= Intent(this, NotificationActivity::class.java)
            intent.putExtra("typeId",typeId)
            intent.putExtra("title",ntitle)
            intent.putExtra("body",nbody)
            startActivity(intent)
        }
        else if (checkFragment == "4") {
            var intent= Intent(this, MainActivity::class.java)
            intent.putExtra("type_id",typeId)
            startActivity(intent)

        }

    }




}