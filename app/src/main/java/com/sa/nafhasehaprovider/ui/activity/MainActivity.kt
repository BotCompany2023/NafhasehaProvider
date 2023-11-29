package com.sa.nafhasehaprovider.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.common.*
import com.sa.nafhasehaprovider.common.util.Utilities
import com.sa.nafhasehaprovider.databinding.ActivityMainBinding
import com.sa.nafhasehaprovider.ui.activity.notification.NotificationActivity
import com.sa.nafhasehaprovider.viewModels.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    private val viewModel: NotificationViewModel by viewModel()


    lateinit var type: String
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusbarTransparent()
        //setupWindowAnimations()
        type = intent.getStringExtra("page").toString()

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


        if (type == "SETTING") {
            navController.navigate(R.id.menuSetting)
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
                                COUNT_NOTIFICATION= it.data!!
                                mViewDataBinding.tvCount.text= it.data!!.toString()
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

    }


}