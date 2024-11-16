package com.safekaro.partner.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ActivityComingSoonBinding
import com.safekaro.partner.ui.common.BaseActivity
import com.safekaro.partner.utils.performBackPressed
import com.safekaro.partner.utils.setLightStatusBar


class ComingSoonActivity : BaseActivity<ActivityComingSoonBinding>(ActivityComingSoonBinding::inflate) {

    companion object {
        fun start(context: Context, title: String = "") {
            context.startActivity(
                Intent(context, ComingSoonActivity::class.java).apply {
                    putExtra("title", title)
                }
            )
        }
    }

    private val title by lazy { intent.getStringExtra("title") ?: getString(R.string.developer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLightStatusBar(window, true)
        setSupportActionBar(binding.toolbar)

        setupViews()
    }

    private fun setupViews() {
        binding.tvTitle.text = title
        binding.ivBack.setOnClickListener { performBackPressed() }
    }

}