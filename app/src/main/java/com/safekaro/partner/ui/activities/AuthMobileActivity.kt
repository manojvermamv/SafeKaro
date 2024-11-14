package com.safekaro.partner.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.safekaro.partner.ui.common.BaseActivity
import com.safekaro.partner.utils.setLightStatusBar
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ActivityAuthMobileBinding
import com.safekaro.partner.ui.common.setupDontHaveAccount
import com.safekaro.partner.ui.common.setupPrivacyPolicy
import com.safekaro.partner.utils.performBackPressed
import com.safekaro.partner.utils.showKeyboardEditText
import com.safekaro.partner.utils.showToast


class AuthMobileActivity : BaseActivity<ActivityAuthMobileBinding>(ActivityAuthMobileBinding::inflate) {

    private val phonePickIntentResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            try {
                val phoneNumber = Identity.getSignInClient(this).getPhoneNumberFromIntent(result.data)
                binding.etMobileNo.setText(phoneNumber.replace(Regex("[\\s-]"), "").takeLast(10))
            } catch(_: Exception) {
                showKeyboardEditText(binding.etMobileNo)
            }
        }

    private fun showPhoneNumberSuggestion() {
        val signInClient = Identity.getSignInClient(this)
        signInClient.getPhoneNumberHintIntent(GetPhoneNumberHintIntentRequest.builder().build())
            .addOnSuccessListener {
                phonePickIntentResultLauncher.launch(IntentSenderRequest.Builder(it).build())
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(window, true)
        setupViews()
        showPhoneNumberSuggestion()
    }

    private fun setupViews() {
        binding.tvPrivacyPolicy.setupPrivacyPolicy()
        binding.tvDontHaveAcc.setupDontHaveAccount(
            getString(R.string.dont_have_an_account),
            getString(R.string.sign_up)
        ) {}

        binding.toolbar.ivBack.setOnClickListener { performBackPressed() }
        binding.btnLogin.setOnClickListener {
            val countryCode = binding.etCountryCode.selectedCountryCode()
            val mobile = binding.etMobileNo.text.toString()

            if (mobile.trim().isNotEmpty()) {
                // onSignInClick
                goToHome(countryCode.trim() + mobile.trim())
            } else {
                showToast(R.string.enter_valid_mobile_number)
            }
        }
        binding.btnSignUpEmail.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun goToHome(mobileNo: String? = null) {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }

}