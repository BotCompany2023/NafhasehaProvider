package com.sa.nafhasehaprovider.di

import com.sa.nafhasehaprovider.ui.generalViewModel.AreasViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.AuthenticationViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.CityViewModel
import com.sa.nafhasehaprovider.ui.generalViewModel.LogOutViewModel
import com.sa.nafhasehaprovider.viewModels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authenticationViewModelModule = module {
    viewModel {
        AuthenticationViewModel(get(),get())
    }
}
val cityViewModelModule = module {
    viewModel {
        CityViewModel(get(), get())
    }
}
val areasViewModelModule = module {
    viewModel {
        AreasViewModel(get(), get())
    }
}

val logoutViewModelModule = module {
    viewModel {
        LogOutViewModel(get(), get())
    }
}

val infoViewModelModule = module {
    viewModel {
        InfoViewModel(get(), get())
    }
}

val faqsViewModelModule = module {
    viewModel {
        FaqsViewModel(get(), get())
    }
}

val notificationViewModelModule = module {
    viewModel {
        NotificationViewModel(get(), get())
    }
}
val homeViewModelModule = module {
    viewModel {
        HomeViewModel(get(), get())
    }
}

val packageModelModule = module {
    viewModel {
        PackageViewModel(get(), get())
    }
}

val walletModelModule = module {
    viewModel {
        WalletViewModel(get(), get())
    }
}

val  myCarModelModule = module {
    viewModel {
        MyCarViewModel(get(), get())
    }
}


val  ordersViewModel = module {
    viewModel {
        OrdersViewModel(get(), get())
    }
}