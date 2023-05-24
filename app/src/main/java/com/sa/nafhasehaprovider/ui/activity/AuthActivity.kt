package com.sa.nafhasehaprovider.ui.activity

import android.os.Bundle
import com.sa.nafhasehaprovider.R
import com.sa.nafhasehaprovider.base.BaseActivity
import com.sa.nafhasehaprovider.databinding.ActivityAuthBinding


class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  window.navigationBarColor = resources.getColor(R.color.back_layout)

    }

}