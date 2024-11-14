package com.safekaro.partner.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.rangedialog.PickerDialog
import com.google.android.material.snackbar.Snackbar
import com.safekaro.partner.R
import com.safekaro.partner.databinding.FragmentHomeBinding
import com.safekaro.partner.model.data.InsurancePolicy
import com.safekaro.partner.ui.activities.ComingSoonActivity
import com.safekaro.partner.ui.adapter.InsurancePoliciesRvAdapter
import com.safekaro.partner.ui.adapter.LoadingViewAdapter
import com.safekaro.partner.ui.adapter.UpcomingMatchesRvAdapter
import com.safekaro.partner.ui.adapter.UserMatchesAdapter
import com.safekaro.partner.ui.common.BaseFragment
import com.safekaro.partner.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Date


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by activityViewModel<MainActivityViewModel>()

    private val insurancePoliciesAdapter by lazy {
        InsurancePoliciesRvAdapter {
            ComingSoonActivity.start(requireContext(), it.title)
        }
    }

    private val loadingViewAdapter by lazy { LoadingViewAdapter() }
    private val upcomingMatchesAdapter by lazy {
        UpcomingMatchesRvAdapter { position, _ ->
            //viewModel.globalAction(GlobalAction.RecyclerItemClick(position))
        }
    }

    private val insurancePolices by lazy {
        mutableListOf(
            InsurancePolicy("Motor", R.drawable.ic_insurance_motor),
            InsurancePolicy("Health", R.drawable.ic_insurance_health),
            InsurancePolicy("Life", R.drawable.ic_insurance_life),
            InsurancePolicy("Pet", R.drawable.ic_insurance_pet),
            InsurancePolicy("Travel", R.drawable.ic_insurance_travel),
            InsurancePolicy("View All", R.drawable.ic_insurance_viewall),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInsurancePolicesRecyclerView()
        setupUpcomingMatchesRecyclerView()

        setupWallet()
        fetchUserData()
        fetchRecentCases()
    }

    private fun setupWallet() {
        binding.includeBalance.apply {
            image.setImageResource(R.drawable.img_wallet_broker)
            title.text = "Balance"
            value.text = getString(R.string.ruppe_symbol_placeholder, "-747,000")
        }
        binding.includeNetPremium.apply {
            image.setImageResource(R.drawable.img_wallet_netpremium)
            title.text = "Net Premium"
            value.text = getString(R.string.ruppe_symbol_placeholder, "2,019,459")
        }

        binding.includeMonthlyPayOut.apply {
            image.setImageResource(R.drawable.img_wallet_finalpremium)
            title.text = "Monthly PayOut"
            value.text = getString(R.string.ruppe_symbol_placeholder, "0")
            valuePaid.text = getString(R.string.wallet_payout_paid, "0")
            valueUnpaid.text = getString(R.string.wallet_payout_unpaid, "0")
        }
        binding.includeTotalPayOut.apply {
            image.setImageResource(R.drawable.img_wallet_finalpremium)
            title.text = "Total PayOut"
            value.text = getString(R.string.ruppe_symbol_placeholder, "10,445,356")
            valuePaid.text = getString(R.string.wallet_payout_paid, "10,438,269")
            valueUnpaid.text = getString(R.string.wallet_payout_unpaid, "7,087")
        }

        binding.ivWalletSummary.setOnClickListener {
            val currentDate = LocalDateTime.now()

            val oneMonthBeforeDate = currentDate.minusMonths(1)
            val oneMonthBeforeInstant = oneMonthBeforeDate.atZone(ZoneId.systemDefault()).toInstant()

            val startDate: Date = DateTimeUtils.toDate(oneMonthBeforeInstant)
            val endDate: Date = DateTimeUtils.toDate(currentDate.atZone(ZoneId.systemDefault()).toInstant())

            val dialog = PickerDialog(requireActivity(), startDate, endDate)
            dialog.setMaxDateRange(endDate,true)
            dialog.setOnRangeSelection { startDate, endDate ->
                println("Start Date: $startDate")
                println("End Date: $endDate")
            }
            dialog.showPicker()
        }
    }

    private fun setupInsurancePolicesRecyclerView() {
        binding.rvInsurancePolices.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.rvInsurancePolices.adapter = insurancePoliciesAdapter

        //val itemSpacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp)
        //binding.rvInsurancePolices.addItemDecoration(HorizontalSpacingItemDecoration(itemSpacing))
        insurancePoliciesAdapter.updateList(ArrayList(insurancePolices))
    }

    private fun setupUpcomingMatchesRecyclerView() {
        binding.rvRecentCases.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecentCases.adapter = ConcatAdapter(upcomingMatchesAdapter, loadingViewAdapter)
        binding.rvRecentCases.addOnEndScrollListener {
//            viewModel.upcomingMatchesRes.getData()?.nextPage?.let { page ->
//                viewModel.upcomingMatchesRes.updatePageIndex(page)
//            }
        }
    }

    private fun fetchUserData() {
        viewModel.userDataRes.getOrObserve(viewLifecycleOwner,
            onLoading = {
                viewModel.triggerProgressBar(it)
                binding.layUserMatches.isInvisible = it
                //binding.offersSlider.isInvisible = it
            },
            onSuccess = {
//                it.walletSummary?.let { wallet -> viewModel.updateWallet(wallet) }
//                it.userMatches?.matchList?.let { matches ->
//                    binding.userMatchesSlider.adapter = UserMatchesAdapter(requireContext(), matches)
//                }
//                it.currentOffers?.offerList?.let { offers ->
//                    repeat (1) { offers.addAll(offers) }
//                    /*binding.offersSlider.adapter =
//                        com.safekaro.partner.ui.adapter.CurrentOffersAdapter(
//                            requireContext(),
//                            offers
//                        )*/
//                }
            },
            onError = {
                //showError(it) { viewModel.userDataRes.refresh() }
            }
        )
    }

    private fun fetchRecentCases() {
//        viewModel.upcomingMatchesRes.getOrObserve(viewLifecycleOwner,
//            onLoading = {
//                if (viewModel.upcomingMatchesRes.getCurrentPage() == 1) {
//                    binding.rvRecentCases.isInvisible = it
//                    binding.tvRecentCases.isInvisible = it
//                } else {
//                    loadingViewAdapter.setLoading(it)
//                }
//            },
//            onSuccess = {
//                val matches = viewModel.upcomingMatchesRes.appendList(it.matchList)
//                upcomingMatchesAdapter.updateList(matches)
//            }
//        )
    }

    private fun showError(message: String? = null, refreshData: () -> Unit) {
        Snackbar.make(binding.root, message ?: "Some Error Occurred", Snackbar.LENGTH_LONG)
            .setAction("Retry") { refreshData() }
            .show()
    }

}