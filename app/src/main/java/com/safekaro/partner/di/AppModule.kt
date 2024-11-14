package com.safekaro.partner.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.safekaro.partner.R
import com.safekaro.partner.model.local.ContentDataSource
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.model.repository.MainRepository
import com.safekaro.partner.ui.activities.AuthActivity
import com.safekaro.partner.viewmodel.AuthViewModel
import com.safekaro.partner.viewmodel.MainActivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { ContentDataSource(androidContext().contentResolver) }
    single { SharedPreferenceManager(androidApplication()) }
    single { MainRepository(get()) }

    viewModel {
        AuthViewModel(androidApplication(), get())
    }
    viewModel {
        MainActivityViewModel(androidApplication(), get())
    }

    /*scope<AuthActivity> {
        scoped {
            GoogleSignIn.getClient(get(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(androidContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build())
        }
    }*/
}