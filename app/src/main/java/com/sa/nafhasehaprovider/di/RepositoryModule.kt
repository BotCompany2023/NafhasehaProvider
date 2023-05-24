package com.sa.nafhasehaprovider.di

import com.sa.nafhasehaprovider.repository.MainRepo
import org.koin.dsl.module

val repoModule = module {
    single {
        MainRepo(get())
    }
}