package com.safekaro.partner.ui.fragments

import android.os.Bundle
import android.view.View
import com.safekaro.partner.databinding.FragmentEmptyBinding
import com.safekaro.partner.ui.common.BaseFragment

class EmptyFragment : BaseFragment<FragmentEmptyBinding>(FragmentEmptyBinding::inflate) {

    private val titleResId: Int? by lazy { arguments?.getInt("titleResId") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleResId?.let {
            binding.tvTitle.text = getString(it)
        }
    }

    companion object {
        // Get New Instance of Fragment
        fun get(titleResId: Int): EmptyFragment {
            val fragment = EmptyFragment()
            fragment.arguments = Bundle().apply {
                putInt("titleResId", titleResId)
            }
            return fragment
        }
    }
}