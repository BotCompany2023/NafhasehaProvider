package com.sa.nafhasehaprovider.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.common.USER_DATA
import com.sa.nafhasehaprovider.databinding.ActivitySplashBinding
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.AuthResponse
import com.sa.nafhasehaprovider.ui.activity.notification.NotificationActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_splash

    private var accessToken: AuthResponse? =null
    private lateinit var orderStep: String
    //private  var accessToken: String? =null
    private val SPLASH_TIME = 2000L
    private lateinit var nbody: String
    private lateinit var ntitle: String
    private lateinit var typeId: String
    private lateinit var checkFragment: String

    //    private val viewModel: CityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeStatusbarTransparent();


        onClick()


    }


    private fun onClick() {

        try {
            accessToken = NafhasehaProviderApp.pref.loadUserData(this, USER_DATA)

            Log.i("TestLoginToken", "tokenn $accessToken")
        }catch (e:Exception){}


        Handler(Looper.myLooper()!!).postDelayed(
            {

                // check first if logged or not
                    if (accessToken != null) {
                        openActivityAndFinish(MainActivity::class.java)
                        Animatoo.animateFade(this);
                        init()
                    } else {
                        Log.i("TestLoginToken", " tokenn is null ")
                        openActivityAndFinish(AuthActivity::class.java)
                        finish()
                        Animatoo.animateFade(this);
                    }


            }, SPLASH_TIME
        )


//        Handler(Looper.myLooper()!!).postDelayed(
//            {
//                // check first if logged or not
//                if (NafhasehaProviderApp.pref.loadUserData(this, USER_DATA)!!.data!!.access_token != null)
//                {
//                    Log.i("TestLoginToken", " tokenn ${NafhasehaProviderApp.pref.authToken}")
//                    openActivityAndFinish(MainActivity::class.java)
//                    Animatoo.animateFade(this);
//                    init()
//                }
//                else {
//                    Log.i("TestLoginToken", " tokenn is null ")
//                    openActivityAndFinish(AuthActivity::class.java)
//                    finish()
//                    Animatoo.animateFade(this);
//                }
//
//            }, SPLASH_TIME
//        )

        val fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        mViewDataBinding.ivBack.animation = fadeInAnim
        mViewDataBinding.ivLogoSplash.animation = fadeInAnim


//        Handler(Looper.getMainLooper()).postDelayed({
//            val mainIntent = Intent(this, OnBoardingActivity::class.java)
//            startActivity(mainIntent)
//            finish()
//        }, 3000)
    }


    private fun makeStatusbarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            //  window.statusBarColor = resources.getColor(R.color.purple_200)
            window.navigationBarColor = resources.getColor(R.color.appColor)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = resources.getColor(R.color.appColor)
        }

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
            var intent= Intent(this, MainActivity::class.java)
            intent.putExtra("type",checkFragment)
            intent.putExtra("type_id",typeId)
            intent.putExtra("order_step",orderStep)
            startActivity(intent)
        }

        else if (checkFragment == "3") {
            var intent= Intent(this, NotificationActivity::class.java)
            intent.putExtra("typeId",typeId)
            intent.putExtra("title",ntitle)
            intent.putExtra("body",nbody)
            startActivity(intent)
        }
    }



}