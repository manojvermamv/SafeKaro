package com.safekaro.partner.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.afsal.rangedialog.PickerDialog
import com.safekaro.partner.R
import com.safekaro.partner.databinding.FragmentHomeBinding
import com.safekaro.partner.model.local.preferences.SharedPreferenceManager
import com.safekaro.partner.model.models.PartnerDashboard
import com.safekaro.partner.ui.activities.ComingSoonActivity
import com.safekaro.partner.ui.adapter.BannerOffersAdapter
import com.safekaro.partner.ui.adapter.InsurancePoliciesRvAdapter
import com.safekaro.partner.ui.adapter.UpcomingMatchesRvAdapter
import com.safekaro.partner.ui.common.BaseFragment
import com.safekaro.partner.ui.common.DashViewPagerProvider
import com.safekaro.partner.ui.common.getBanners
import com.safekaro.partner.ui.common.getInsurancePolices
import com.safekaro.partner.ui.common.getMyRenewals
import com.safekaro.partner.ui.common.getOthers
import com.safekaro.partner.ui.common.showError
import com.safekaro.partner.utils.formatDateApi
import com.safekaro.partner.utils.observe
import com.safekaro.partner.viewmodel.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Date


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by activityViewModel<MainActivityViewModel>()
    private val prefs: SharedPreferenceManager by inject()

    private var startDate: Date? = null
    private var endDate: Date? = null

    private val upcomingMatchesAdapter by lazy {
        UpcomingMatchesRvAdapter { position, _ ->
            //viewModel.globalAction(GlobalAction.RecyclerItemClick(position))
        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupInsurancePolicesRecyclerView()
        setupMyRenewalRecyclerView()
        setupOthersRecyclerView()

        fetchDashboardData()
        fetchUserData()
    }

    private fun setupViews() {
        binding.tvName.text = prefs.userData().name
        val dashViewPager = DashViewPagerProvider()
        dashViewPager.registerViewPagerAdapter(requireContext(), lifecycle, childFragmentManager)
        dashViewPager.setupBottomNavigation(binding.viewPager, binding.tabLayout, onPageChangeCallback)

        // setup date range
        val currentDate = LocalDateTime.now()
        val oneMonthBeforeDate = currentDate.minusMonths(1)
        val maxDate = DateTimeUtils.toDate(currentDate.atZone(ZoneId.systemDefault()).toInstant())

        startDate = DateTimeUtils.toDate(oneMonthBeforeDate.atZone(ZoneId.systemDefault()).toInstant())
        endDate = DateTimeUtils.toDate(currentDate.atZone(ZoneId.systemDefault()).toInstant())
        //viewModel.setDashBoardDate(startDate, endDate)

        binding.layFilters.setOnClickListener {
            val dialog = PickerDialog(requireActivity(), startDate, endDate)
            dialog.setMaxDateRange(maxDate,true)
            dialog.setOnRangeSelection { startDate, endDate ->
                this.startDate = startDate
                this.endDate = endDate
                fetchDashboardData()
            }
            dialog.showPicker()
        }
    }

    private fun setupWallet(partnerDashboard: PartnerDashboard) {
        binding.includeMonthlyPayOut.apply {
            image.setImageResource(R.drawable.img_wallet_finalpremium)
            title.text = "Monthly PayOut"
            value.text = getString(R.string.ruppe_symbol_placeholder, partnerDashboard.commissions.Monthly_Commission.toString())
            valuePaid.text = getString(R.string.wallet_payout_paid, partnerDashboard.commissions.Monthly_Paid_Amount.toString())
            valueUnpaid.text = getString(R.string.wallet_payout_unpaid, partnerDashboard.commissions.Monthly_UnPaid_Amount.toString())
        }
        binding.includeTotalPayOut.apply {
            image.setImageResource(R.drawable.img_wallet_finalpremium)
            title.text = "Total PayOut"
            value.text = getString(R.string.ruppe_symbol_placeholder, partnerDashboard.commissions.Total_Commission.toString())
            valuePaid.text = getString(R.string.wallet_payout_paid, partnerDashboard.commissions.Total_Paid_Amount.toString())
            valueUnpaid.text = getString(R.string.wallet_payout_unpaid, partnerDashboard.commissions.Total_UnPaid_Amount.toString())
        }
    }

    private fun setupInsurancePolicesRecyclerView() {
        val insurancePoliciesAdapter = InsurancePoliciesRvAdapter {
            ComingSoonActivity.start(requireContext(), it.title)
        }
        binding.rvInsurancePolices.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvInsurancePolices.adapter = insurancePoliciesAdapter
        insurancePoliciesAdapter.updateList(ArrayList(getInsurancePolices()))
        //val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp)
        //binding.rvInsurancePolices.addItemDecoration(HorizontalSpacingItemDecoration(itemSpacing))
    }

    private fun setupMyRenewalRecyclerView() {
        val myRenewalAdapter = InsurancePoliciesRvAdapter {
            ComingSoonActivity.start(requireContext(), it.title)
        }
        binding.rvMyRenewal.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvMyRenewal.adapter = myRenewalAdapter
        myRenewalAdapter.updateList(ArrayList(getMyRenewals()))
    }

    private fun setupOthersRecyclerView() {
        val othersAdapter = InsurancePoliciesRvAdapter {
            ComingSoonActivity.start(requireContext(), it.title)
        }
        binding.rvOthers.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvOthers.adapter = othersAdapter
        othersAdapter.updateList(ArrayList(getOthers()))
    }

    private fun fetchDashboardData() {
        val mStartDate = formatDateApi(startDate)
        val mEndDate = formatDateApi(endDate)
        viewModel.getDashboardData(prefs.userData().partnerId ?: "", mStartDate, mEndDate).observe(viewLifecycleOwner,
            onError = { binding.showError(it) },
            onLoading = { viewModel.triggerProgressBar(it) },
            onSuccess = { data ->
                data.data[0].let { setupWallet(it) }
            },
        )
    }

    private fun fetchUserData() {
        binding.bannerSlider.adapter = BannerOffersAdapter(ArrayList(getBanners()))

//        viewModel.userDataRes.getOrObserve(viewLifecycleOwner,
//            onLoading = {
//                viewModel.triggerProgressBar(it)
//                binding.layUserMatches.isInvisible = it
//            },
//            onSuccess = {
////                it.walletSummary?.let { wallet -> viewModel.updateWallet(wallet) }
////                it.userMatches?.matchList?.let { matches ->
////                    binding.userMatchesSlider.adapter = UserMatchesAdapter(requireContext(), matches)
////                }
//            },
//            onError = {
//                //showError(it) { viewModel.userDataRes.refresh() }
//            }
//        )
    }

}