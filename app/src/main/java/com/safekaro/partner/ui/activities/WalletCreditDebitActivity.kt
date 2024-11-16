package com.safekaro.partner.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.rangedialog.PickerDialog
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ActivityWalletCreditDebitBinding
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.ui.adapter.InsurancePoliciesRvAdapter
import com.safekaro.partner.ui.adapter.WalletCreditDebitRvAdapter
import com.safekaro.partner.ui.common.BaseActivity
import com.safekaro.partner.ui.common.getOthers
import com.safekaro.partner.ui.common.showError
import com.safekaro.partner.utils.formatDateApi
import com.safekaro.partner.utils.observe
import com.safekaro.partner.utils.performBackPressed
import com.safekaro.partner.utils.setLightStatusBar
import com.safekaro.partner.viewmodel.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Date


class WalletCreditDebitActivity : BaseActivity<ActivityWalletCreditDebitBinding>(ActivityWalletCreditDebitBinding::inflate) {

    companion object {
        fun start(context: Context, title: String = "") {
            context.startActivity(
                Intent(context, WalletCreditDebitActivity::class.java).apply {
                    putExtra("title", title)
                }
            )
        }
    }

    private val title by lazy { intent.getStringExtra("title") ?: getString(R.string.developer) }

    private val viewModel by viewModel<MainActivityViewModel>()
    private val prefs: SharedPreferenceManager by inject()

    private var startDate: Date? = null
    private var endDate: Date? = null

    private val mAdapter = WalletCreditDebitRvAdapter {
        //ComingSoonActivity.start(this, it.title)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLightStatusBar(window, true)
        setSupportActionBar(binding.toolbar)

        setupViews()
        setupCreditDebitRecyclerView()
        fetchWalletCreditDebit()
    }

    private fun setupViews() {
        binding.tvTitle.text = title
        binding.ivBack.setOnClickListener { performBackPressed() }

        // setup date range
        val currentDate = LocalDateTime.now()
        val oneMonthBeforeDate = currentDate.minusMonths(1)
        val maxDate = DateTimeUtils.toDate(currentDate.atZone(ZoneId.systemDefault()).toInstant())

        startDate = DateTimeUtils.toDate(oneMonthBeforeDate.atZone(ZoneId.systemDefault()).toInstant())
        endDate = DateTimeUtils.toDate(currentDate.atZone(ZoneId.systemDefault()).toInstant())

        binding.layFilters.setOnClickListener {
            val dialog = PickerDialog(this, startDate, endDate)
            dialog.setMaxDateRange(maxDate,true)
            dialog.setOnRangeSelection { startDate, endDate ->
                this.startDate = startDate
                this.endDate = endDate
                fetchWalletCreditDebit()
            }
            dialog.showPicker()
        }
    }

    private fun setupCreditDebitRecyclerView() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = mAdapter
    }

    private fun fetchWalletCreditDebit() {
        val mStartDate = formatDateApi(startDate)
        val mEndDate = formatDateApi(endDate)
        viewModel.getWalletCreditDebit(prefs.userData().partnerId ?: "", mStartDate, mEndDate).observe(this,
            onError = { binding.showError(it) },
            onLoading = { binding.progressBar.isVisible = it },
            onSuccess = { data ->
                data.data?.let {
                    mAdapter.updateList(ArrayList(it))
                }
            },
        )
    }

}