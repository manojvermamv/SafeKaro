package com.safekaro.partner.ui.common

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.safekaro.partner.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.safekaro.partner.databinding.TabItemDashBinding
import com.safekaro.partner.ui.fragments.DashSummaryFragment

class DashViewPagerProvider {

    private lateinit var inflater: LayoutInflater
    private lateinit var adapter: ViewPagerAdapter

    private val defaultIndex = 0

    private val iconRes = intArrayOf(
        R.drawable.ic_tab_performance12,
        R.drawable.ic_tab_performance2,
        //R.drawable.ic_tab_performance3,
    )
    private val fragments: MutableList<BaseFragment<out ViewBinding>> by lazy {
        mutableListOf(
            DashSummaryFragment.get(1),
            DashSummaryFragment.get(2),
            //DashSummaryFragment.get(3),
        )
    }

    // First Call
    fun registerViewPagerAdapter(context: Context, lifecycle: Lifecycle, manager: FragmentManager) {
        this.inflater = LayoutInflater.from(context)
        this.adapter = ViewPagerAdapter(manager, fragments, lifecycle)
    }

    // Second Call
    fun setupBottomNavigation(viewPager: ViewPager2, tabLayout: TabLayout, pageChangeCallback: ViewPager2.OnPageChangeCallback?) {
        isAdapterInitialized()
        viewPager.isUserInputEnabled = false
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = adapter
        (viewPager.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.setBottomNavHover(tab.position, true)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.setBottomNavHover(tab.position, false)
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        TabLayoutMediator(tabLayout, viewPager, true , false) { tab, position ->
            tab.customView = TabItemDashBinding.inflate(inflater).apply {
                //title.setText(titleRes[position])
                icon.setImageResource(iconRes[position])
            }.root
        }.attach()

        if (pageChangeCallback != null) {
            viewPager.registerOnPageChangeCallback(pageChangeCallback)
        }
        viewPager.currentItem = defaultIndex
    }

    fun switchFragment(viewPager: ViewPager2, fragment: BaseFragment<out ViewBinding>) {
        var mIndex = 0
        fragments.forEachIndexed { index, baseFragment ->
            if (baseFragment.javaClass.simpleName == fragment.javaClass.simpleName) {
                mIndex = index
            }
        }
        viewPager.setCurrentItem(mIndex, false)
        isAdapterInitialized()
        adapter.refreshFragment(mIndex, fragment)
    }

    private fun View.setBottomNavHover(position: Int, isSelected: Boolean) {
        findViewById<ImageView>(R.id.icon)?.apply {
            val color = if (isSelected) R.color.secondary else R.color.nav_item_tint_default
            setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
            //background = if (isSelected) getDrawableCompat(R.drawable.bg_nav_item_selected) else null
        }
    }

    private fun isAdapterInitialized() {
        if (this::adapter.isInitialized.not()) {
            throw IllegalStateException("ViewPagerAdapter not initialized")
        }
    }

}