package com.safekaro.partner.ui.common

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.safekaro.partner.R
import com.safekaro.partner.ui.fragments.EmptyFragment
import com.safekaro.partner.ui.fragments.HomeFragment
import com.safekaro.partner.utils.getDrawableCompat
import com.safekaro.partner.utils.setGradientTextColor
import com.safekaro.partner.utils.setTextColorByRes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.safekaro.partner.databinding.TabItemNavBinding


// Using like kotlin delegates
interface ViewPagerProvider {
    fun registerViewPagerAdapter(context: Context, owner: LifecycleOwner, manager: FragmentManager)
    fun setupBottomNavigation(viewPager: ViewPager2, tabLayout: TabLayout, pageChangeCallback: ViewPager2.OnPageChangeCallback? = null)
    fun switchFragment(viewPager: ViewPager2, fragment: BaseFragment<out ViewBinding>)
}

class ViewPagerProviderImpl : ViewPagerProvider, LifecycleEventObserver {

    private lateinit var lifecycle: Lifecycle
    private lateinit var inflater: LayoutInflater
    private lateinit var adapter: ViewPagerAdapter

    private val defaultIndex = 0
    private val titleRes = intArrayOf(
        R.string.home,
        R.string.team,
        R.string.my_policy,
        R.string.my_wallet,
    )
    private val iconRes = intArrayOf(
        R.drawable.ic_nav_home_outline,
        R.drawable.ic_nav_team,
        R.drawable.ic_nav_policy,
        R.drawable.ic_nav_wallet,
    )
    /*private val iconRes = intArrayOf(
        R.drawable.ic_home_selector,
        R.drawable.ic_renewals_selector,
        R.drawable.ic_cases_selector,
        R.drawable.ic_support_selector,
        R.drawable.ic_more_selector,
    )*/

    private val fragments by lazy {
        mutableListOf(
            HomeFragment(),
            EmptyFragment.get(R.string.team),
            EmptyFragment.get(R.string.my_policy),
            EmptyFragment.get(R.string.my_wallet),
        )
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> println("User created the screen")
            Lifecycle.Event.ON_START -> println("User started the screen")
            Lifecycle.Event.ON_RESUME -> println("User resumed(opened) the screen")
            Lifecycle.Event.ON_PAUSE -> println("User paused(leaves) the screen")
            Lifecycle.Event.ON_STOP -> println("User stopped the screen")
            Lifecycle.Event.ON_DESTROY -> println("User destroyed the screen")
            else -> Unit
        }
    }

    override fun registerViewPagerAdapter(context: Context, owner: LifecycleOwner, manager: FragmentManager) {
        this.lifecycle = owner.lifecycle
        this.inflater = LayoutInflater.from(context)
        this.adapter = ViewPagerAdapter(manager, fragments, lifecycle)
        lifecycle.addObserver(this)
    }

    override fun setupBottomNavigation(viewPager: ViewPager2, tabLayout: TabLayout, pageChangeCallback: ViewPager2.OnPageChangeCallback?) {
        isAdapterInitialized()
        viewPager.isUserInputEnabled = false
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = 4
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
            tab.customView = TabItemNavBinding.inflate(inflater).apply {
                title.setText(titleRes[position])
                icon.setImageResource(iconRes[position])
            }.root
        }.attach()

        if (pageChangeCallback != null) {
            viewPager.registerOnPageChangeCallback(pageChangeCallback)
        }
        viewPager.currentItem = defaultIndex
    }

    override fun switchFragment(viewPager: ViewPager2, fragment: BaseFragment<out ViewBinding>) {
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
            val size = if (isSelected) com.intuit.sdp.R.dimen._27sdp else com.intuit.sdp.R.dimen._27sdp
            val color = if (isSelected) R.color.primary else R.color.nav_item_tint_default
            setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
            //background = if (isSelected) getDrawableCompat(R.drawable.bg_nav_item_selected) else null
            layoutParams = layoutParams.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = resources.getDimension(size).toInt()
            }
            requestLayout()
        }
        findViewById<TextView>(R.id.title)?.apply {
            if (isSelected)
                setGradientTextColor(lifecycle, R.color.nav_item_tint_selected, R.color.nav_item_tint_selected)
            else
                setTextColorByRes(R.color.nav_item_tint_default)
        }
    }

    private fun isAdapterInitialized() {
        if (this::adapter.isInitialized.not()) {
            throw IllegalStateException("ViewPagerAdapter not initialized")
        }
    }

}