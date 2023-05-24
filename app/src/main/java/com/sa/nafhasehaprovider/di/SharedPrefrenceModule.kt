package com.sa.nafhasehaprovider.di

import com.sa.nafhasehaprovider.common.sharedprefrence.PreferencesUtils
import org.koin.dsl.module

val sharedPreferencesModule = module {
    single {
        PreferencesUtils(get())
    }
}