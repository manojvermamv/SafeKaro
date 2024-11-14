package com.safekaro.partner.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.core.view.isVisible
import com.safekaro.partner.ui.common.BaseActivity
import com.safekaro.partner.utils.setLightStatusBar
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ActivityAuthEmailBinding
import com.safekaro.partner.model.data.LoginUserRequest
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.ui.common.setupDontHaveAccount
import com.safekaro.partner.ui.common.setupPrivacyPolicy
import com.safekaro.partner.utils.SingleEvent
import com.safekaro.partner.utils.observe
import com.safekaro.partner.utils.observeEvent
import com.safekaro.partner.utils.performBackPressed
import com.safekaro.partner.utils.showToast
import com.safekaro.partner.viewmodel.AuthViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthEmailLoginActivity : BaseActivity<ActivityAuthEmailBinding>(ActivityAuthEmailBinding::inflate) {

    private var isPasswordVisible = false

    private val viewModel by viewModel<AuthViewModel>()
    private val prefs: SharedPreferenceManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(window, true)
        setupViews()

        observeEvent(viewModel.progressBar, ::triggerProgressBar)
    }

    private fun triggerProgressBar(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            binding.progressBar.isVisible = it
        }
    }

    private fun setupViews() {
        binding.tvPrivacyPolicy.setupPrivacyPolicy()
        binding.tvDontHaveAcc.setupDontHaveAccount(
            getString(R.string.dont_have_an_account),
            getString(R.string.sign_up)
        ) {}

        binding.toolbar.ivBack.setOnClickListener { performBackPressed() }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailMobile.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.trim().length > 3 && password.trim().length > 3) {
                onSignInClick(LoginUserRequest(email.trim(), password.trim()))
            } else {
                showToast(R.string.enter_valid_mobile_number)
            }
        }
        binding.btnSignUpEmail.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.ivTogglePassword.setOnClickListener {
            // Toggle the password visibility
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Show password
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ivTogglePassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                // Hide password
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ivTogglePassword.setImageResource(R.drawable.ic_eye)
            }

            // Move the cursor to the end of the text
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

    private fun onSignInClick(loginRequest: LoginUserRequest) {
        viewModel.loginWithMail(loginRequest).observe(this,
            onError = { showToast(it) },
            onLoading = { viewModel.triggerProgressBar(it) },
            onSuccess = {
                if (it.status == "success") {
                    prefs.userData(it)
                    goToHome()
                } else {
                    showToast(it.message)
                }
            },
        )
    }

}