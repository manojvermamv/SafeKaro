package com.safekaro.partner.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.safekaro.partner.databinding.ActivityAuthBinding
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.ui.common.BaseActivity
import com.safekaro.partner.ui.common.setupPrivacyPolicy
import com.safekaro.partner.utils.setLightStatusBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class AuthActivity : BaseActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    private var keepSplashAlive = true
    private val prefs: SharedPreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //Keep returning false to Should Keep On Screen until ready to begin.
        splashScreen.setKeepOnScreenCondition { keepSplashAlive }

        // Will cause a second process to run on the main thread
        CoroutineScope(Dispatchers.Main).launch {
            delay(200L)
            authenticateUser()
        }

        setLightStatusBar(window, true)
        setupViews()
    }

    private fun setupViews() {
        binding.tvPrivacyPolicy.setupPrivacyPolicy()
        binding.btnLoginWithMobile.setOnClickListener {
            startActivity(Intent(this, AuthEmailLoginActivity::class.java))
        }
        binding.btnSignUpEmail.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun authenticateUser() {
        if (prefs.userData().id.isNullOrEmpty().not() && prefs.userData().accessToken.isNullOrEmpty().not()) {
            keepSplashAlive = false
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()
        } else {
            keepSplashAlive = false
        }
    }

}