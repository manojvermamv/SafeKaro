package com.safekaro.partner.ui.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ActivityHomeBinding
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.ui.common.BaseActivity
import com.safekaro.partner.ui.common.GlobalAction
import com.safekaro.partner.ui.common.UiAction
import com.safekaro.partner.ui.common.ViewPagerProvider
import com.safekaro.partner.ui.common.ViewPagerProviderImpl
import com.safekaro.partner.ui.dialogs.CustomMaterialDialog
import com.safekaro.partner.utils.NetworkConnection
import com.safekaro.partner.utils.SingleEvent
import com.safekaro.partner.utils.awaitLayoutChange
import com.safekaro.partner.utils.isAllPermissionsGranted
import com.safekaro.partner.utils.observeEvent
import com.safekaro.partner.utils.onBackPressed
import com.safekaro.partner.utils.registerForMultiplePermissionsResult
import com.safekaro.partner.utils.requestPermission
import com.safekaro.partner.utils.setGradientTextColor
import com.safekaro.partner.utils.setLightStatusBar
import com.safekaro.partner.utils.showToast
import com.safekaro.partner.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.system.exitProcess


class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate),
    NavigationView.OnNavigationItemSelectedListener,
    ViewPagerProvider by ViewPagerProviderImpl() {

    private val viewModel by viewModel<MainActivityViewModel>()
    private val prefs: SharedPreferenceManager by inject()

    private val noInternetDialog by lazy {
        CustomMaterialDialog(this).apply {
            setOnConfirmClickListener { exitProcess(0) }
            setOnDismissListener {
                viewModel.userDataRes.refresh()
                //viewModel.upcomingMatchesRes.resetData()
            }
        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val set = ConstraintSet()
            binding.bgView.apply {
                layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                    matchConstraintPercentHeight = if (position == 0) 0.99f else 0.99f
                }
            }
            set.applyTo(binding.constraintLayout)
        }
    }

    private val relationalMsg = "This app needs notification permissions to get updates."
    private val permissions by lazy {
        mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toList()
    }
    private val permissionsLauncher = registerForMultiplePermissionsResult { results ->
        if (results.isAllPermissionsGranted().not()) {
            showToast("This app needs notification permissions! Go to settings and grant all permissions.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar(window, true)
        setSupportActionBar(binding.toolbar)
        onBackPressed {
            if (binding.viewPager.currentItem != 0) {
                binding.viewPager.setCurrentItem(0, false)
            } else {
                finish()
            }
            true
        }

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.apply {
            isDrawerIndicatorEnabled = false
            setHomeAsUpIndicator(R.drawable.ic_menu)
            syncState()
        }
        binding.navView.setNavigationItemSelectedListener(this)
        binding.toolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.navView)) {
                binding.drawerLayout.closeDrawer(binding.navView)
            } else {
                binding.drawerLayout.openDrawer(binding.navView)
            }
        }

        binding.tvName.text = prefs.userData().name
        binding.navView.getHeaderView(0)?.let {
            it.findViewById<TextView>(R.id.tvUsername)?.text = prefs.userData().name
            it.findViewById<TextView>(R.id.tvUserMail)?.text = prefs.userData().email
        }

        setupViews()
        updateTextWithGradient()
        registerViewPagerAdapter(this, this, supportFragmentManager)
        setupBottomNavigation(binding.viewPager, binding.tabLayout, onPageChangeCallback)

        observeActions()
        observeEvent(viewModel.progressBar, ::triggerProgressBar)
        //requestPermission(permissions, relationalMsg, permissionsLauncher)

        NetworkConnection(applicationContext).observe(this) { isConnected ->
            noInternetDialog.apply {
                binding.viewPager.isInvisible = !isConnected
                if (isConnected) dismiss() else show()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                prefs.clear()
                restartApp()
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }


    private fun restartApp() {
        val intent = Intent(applicationContext, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun setupViews() {
        binding.tvName.text = getString(R.string.hi_developer)
        binding.layBalance.setOnClickListener { viewModel.globalAction(GlobalAction.WalletClick) }
        binding.ivNotification.setOnClickListener { viewModel.globalAction(GlobalAction.NotificationClick) }
        binding.ivUser.setOnClickListener { viewModel.globalAction(GlobalAction.ProfileClick) }
    }

    private fun updateTextWithGradient(balance: Int? = null) {
        binding.tvBalance.text = (balance ?: 0).toString()
        lifecycle.coroutineScope.launch {
            binding.tvBalancePrefix.awaitLayoutChange()
            binding.tvBalancePrefix.setGradientTextColor(R.color.accent_color, R.color.accent_color)
            binding.tvBalance.awaitLayoutChange()
            binding.tvBalance.setGradientTextColor(R.color.accent_color, R.color.accent_color)
        }
    }

    private fun observeActions() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.globalAction.collect { action ->
                    when (action) {
                        is GlobalAction.WalletClick -> println("Wallet Clicked")
                        is GlobalAction.NotificationClick -> ComingSoonActivity.start(this@HomeActivity, "Notifications")
                        is GlobalAction.ProfileClick -> ComingSoonActivity.start(this@HomeActivity, "Profile")
                        else -> Unit
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.uiAction.collect { action ->
                when (action) {
                    is UiAction.UpdateWallet -> {}//updateTextWithGradient(action.data.cashBalance)
                    else -> Unit
                }
            }
        }
    }

    private fun triggerProgressBar(event: SingleEvent<Boolean>) {
        event.getContentIfNotHandled()?.let {
            binding.progressBar.isVisible = it
        }
    }

}