package com.safekaro.partner.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.safekaro.partner.R
import com.safekaro.partner.databinding.FragmentDashSummaryBinding
import com.safekaro.partner.model.data.PerformanceData
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.model.models.PartnerDashboard
import com.safekaro.partner.ui.adapter.PerformanceRvAdapter
import com.safekaro.partner.ui.common.BaseFragment
import com.safekaro.partner.utils.observe
import com.safekaro.partner.utils.showToast
import com.safekaro.partner.viewmodel.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DashSummaryFragment : BaseFragment<FragmentDashSummaryBinding>(FragmentDashSummaryBinding::inflate) {

    private val id: Int? by lazy { arguments?.getInt("id") }

    private val viewModel by activityViewModel<MainActivityViewModel>()

    private val prefs: SharedPreferenceManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvOne.isVisible = false
        binding.rvTwo.isVisible = false
        binding.rvThree.isVisible = false
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        viewModel.getDashboardData(prefs.userData().partnerId ?: "", "2024-06-01", "2024-11-14").observe(viewLifecycleOwner,
            onError = { showToast(it) },
            onLoading = { viewModel.triggerProgressBar(it) },
            onSuccess = { data ->
                data.data[0].let { setupWallet(it) }
            },
        )
    }

    private fun setupWallet(partnerDashboard: PartnerDashboard) {
        when(id) {
            1 -> {
                binding.rvOne.isVisible = true
                binding.rvTwo.isVisible = false
                binding.rvThree.isVisible = false
                setupRecyclerViewOne(partnerDashboard)
            }
            2 -> {
                binding.rvOne.isVisible = false
                binding.rvTwo.isVisible = true
                binding.rvThree.isVisible = false
                setupRecyclerViewTwo(partnerDashboard)
            }
            3 -> {
                binding.rvOne.isVisible = false
                binding.rvTwo.isVisible = false
                binding.rvThree.isVisible = true
                setupRecyclerViewThree(partnerDashboard)
            }
        }

//        binding.includeNetPremium.apply {
//            image.setImageResource(R.drawable.img_wallet_netpremium)
//            title.text = "Net Premium"
//            value.text = getString(R.string.ruppe_symbol_placeholder, partnerDashboard.premiums.netPremium.toString())
//        }
//
//        binding.includeBalance.apply {
//            image.setImageResource(R.drawable.img_wallet_broker)
//            title.text = "Balance"
//            value.text = getString(R.string.ruppe_symbol_placeholder, partnerDashboard.commissions.balance.toString())
//        }
//        binding.includeMonthlyPayOut.apply {
//            image.setImageResource(R.drawable.img_wallet_finalpremium)
//            title.text = "Monthly PayOut"
//            value.text = getString(R.string.ruppe_symbol_placeholder, partnerDashboard.commissions.monthlyCommission.toString())
//            valuePaid.text = getString(R.string.wallet_payout_paid, partnerDashboard.commissions.monthlyPaidAmount.toString())
//            valueUnpaid.text = getString(R.string.wallet_payout_unpaid, partnerDashboard.commissions.monthlyUnPaidAmount.toString())
//        }
//        binding.includeTotalPayOut.apply {
//            image.setImageResource(R.drawable.img_wallet_finalpremium)
//            title.text = "Total PayOut"
//            value.text = getString(R.string.ruppe_symbol_placeholder, partnerDashboard.commissions.totalCommission.toString())
//            valuePaid.text = getString(R.string.wallet_payout_paid, partnerDashboard.commissions.totalPaidAmount.toString())
//            valueUnpaid.text = getString(R.string.wallet_payout_unpaid, partnerDashboard.commissions.totalUnPaidAmount.toString())
//        }
    }

    private fun setupRecyclerViewOne(data: PartnerDashboard) {
        println(data.commissions.getSerializedName())
        println(data.commissions.getSerializedNameValue(data.commissions.getSerializedName()))

        val dataList = mutableListOf(
            PerformanceData("Motor", data.policyCounts.motor, R.drawable.ic_per_policies),
            PerformanceData("Premium", data.premiums.netPremium, R.drawable.img_wallet_netpremium),
            PerformanceData("Balance", data.commissions.Balance, R.drawable.ic_per_premium),

            PerformanceData("Monthly PayOut", data.commissions.Monthly_Commission, R.drawable.img_wallet_finalpremium),
            PerformanceData("Monthly Paid PayOut", data.commissions.Monthly_Paid_Amount, R.drawable.img_wallet_payout),
            PerformanceData("Monthly UnPaid PayOut", data.commissions.Monthly_UnPaid_Amount, R.drawable.img_wallet_payin),

            PerformanceData("Total PayOut", data.commissions.Total_Commission, R.drawable.img_wallet_finalpremium),
            PerformanceData("Total Paid PayOut", data.commissions.Total_Paid_Amount, R.drawable.img_wallet_payout),
            PerformanceData("Total UnPaid PayOut", data.commissions.Total_UnPaid_Amount, R.drawable.img_wallet_payin),
        )

        val mAdapter = PerformanceRvAdapter()
        binding.rvOne.setHasFixedSize(true)
        binding.rvOne.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvOne.adapter = mAdapter
        mAdapter.updateList(ArrayList(dataList))
    }

    private fun setupRecyclerViewTwo(data: PartnerDashboard) {
        val dataList = mutableListOf(
            PerformanceData("Total Booking", data.bookingRequests.Total_Booking, R.drawable.img_wallet_payout),
            PerformanceData("Accepted Booking", data.bookingRequests.Accepted_Booking, R.drawable.img_wallet_payout),
            PerformanceData("Requested Booking", data.bookingRequests.Requested_Booking, R.drawable.img_wallet_finalpremium),
            PerformanceData("Rejected Booking", data.bookingRequests.Booked_Booking, R.drawable.img_wallet_payin),
            PerformanceData("Rejected Booking", data.bookingRequests.Rejected_Booking, R.drawable.img_wallet_payin),
            PerformanceData("Total Lead", data.leadCounts.Total_Lead, R.drawable.img_wallet_payin),
        )

        val mAdapter = PerformanceRvAdapter()
        binding.rvTwo.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTwo.adapter = mAdapter
        mAdapter.updateList(ArrayList(dataList))
    }

    private fun setupRecyclerViewThree(data: PartnerDashboard) {
    }

    companion object {
        // Get New Instance of Fragment
        fun get(id: Int): DashSummaryFragment {
            val fragment = DashSummaryFragment()
            fragment.arguments = Bundle().apply {
                putInt("id", id)
            }
            return fragment
        }
    }
}