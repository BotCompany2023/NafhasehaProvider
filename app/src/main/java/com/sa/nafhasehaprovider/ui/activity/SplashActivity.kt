package com.sa.nafhasehaprovider.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.app.NafhasehaProviderApp
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.common.FIRST_TIME
import com.sa.nafhasehaprovider.common.USER_DATA
import com.sa.nafhasehaprovider.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_splash

    private val SPLASH_TIME = 4000L

    //    private val viewModel: CityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeStatusbarTransparent();

        onClick()


    }


    private fun onClick() {


        Handler(Looper.myLooper()!!).postDelayed(
            {
                // here check about first time or not
                // if first time
                if (NafhasehaProviderApp.pref.getBoolean(FIRST_TIME, false)) {
                    // check first if logged or not
                    if (NafhasehaProviderApp.pref.authToken != null
                    ) {
                        Log.i("TestLoginToken", " tokenn ${NafhasehaProviderApp.pref.authToken}")
                        openActivityAndFinish(MainActivity::class.java)
                        Animatoo.animateFade(this);
                    } else {
                        Log.i("TestLoginToken", " tokenn is null ")
                        openActivityAndFinish(AuthActivity::class.java)
                        finish()
                        Animatoo.animateFade(this);
                    }
                }
                // else not first time
                else {
                    openActivityAndFinish(AuthActivity::class.java)
                }

            }, SPLASH_TIME
        )

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
}