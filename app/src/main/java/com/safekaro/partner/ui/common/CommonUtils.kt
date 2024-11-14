package com.safekaro.partner.ui.common

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.safekaro.partner.R

class CommonUtils {
}

fun TextView.setupPrivacyPolicy() {
    val mColor = context.getColor(R.color.primary)
    val fullText = context.getString(R.string.by_continui)
    val firstText = context.getString(R.string.by_continui_1)
    val secondText = context.getString(R.string.by_continui_2)

    val spannableString = SpannableString(fullText)
    val privacyPolicyStart = fullText.indexOf(firstText)
    val privacyPolicyEnd = privacyPolicyStart + firstText.length
    spannableString.setSpan(ForegroundColorSpan(mColor), privacyPolicyStart, privacyPolicyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    // Make "Privacy Policy" clickable
    spannableString.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            // Handle click for Privacy Policy clicked
        }
    }, privacyPolicyStart, privacyPolicyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    val termsOfUseStart = fullText.indexOf(secondText)
    val termsOfUseEnd = termsOfUseStart + secondText.length
    spannableString.setSpan(ForegroundColorSpan(mColor), termsOfUseStart, termsOfUseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    // Make "Terms of Use" clickable
    spannableString.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            // Handle click for Terms of Use clicked
        }
    }, termsOfUseStart, termsOfUseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = spannableString
    movementMethod = android.text.method.LinkMovementMethod.getInstance()
}

fun TextView.setupDontHaveAccount(fullText: String, clickableText: String, onClick: () -> Unit) {
    val mColor = context.getColor(R.color.secondary)

    val spannableString = SpannableString(fullText)
    val spanContentStart = fullText.indexOf(clickableText)
    val spanContentEnd = spanContentStart + clickableText.length
    spannableString.setSpan(ForegroundColorSpan(mColor), spanContentStart, spanContentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.setSpan(StyleSpan(Typeface.BOLD), spanContentStart, spanContentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    spannableString.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) { onClick() }
    }, spanContentStart, spanContentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = spannableString
    movementMethod = android.text.method.LinkMovementMethod.getInstance()
}