package com.safekaro.partner.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.safekaro.partner.model.data.InsurancePolicy
import com.safekaro.partner.model.data.PerformanceData
import com.safekaro.partner.model.models.CreditDebit
import com.safekaro.partner.model.models.Match

class InsurancePolicyDiffUtilCallback : DiffUtil.ItemCallback<InsurancePolicy>() {
    override fun areItemsTheSame(oldItem: InsurancePolicy, newItem: InsurancePolicy): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: InsurancePolicy, newItem: InsurancePolicy): Boolean {
        return oldItem.title == newItem.title
    }
}

class PerformanceDataDiffUtilCallback : DiffUtil.ItemCallback<PerformanceData>() {
    override fun areItemsTheSame(oldItem: PerformanceData, newItem: PerformanceData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PerformanceData, newItem: PerformanceData): Boolean {
        return oldItem.title == newItem.title
    }
}


class CreditDebitDiffUtilCallback : DiffUtil.ItemCallback<CreditDebit>() {
    override fun areItemsTheSame(oldItem: CreditDebit, newItem: CreditDebit): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CreditDebit, newItem: CreditDebit): Boolean {
        return oldItem.Id == newItem.Id
    }
}


class MatchDiffUtilCallback : DiffUtil.ItemCallback<Match>() {
    override fun areItemsTheSame(oldItem: Match, newItem: Match): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Match, newItem: Match): Boolean {
        return oldItem.id == newItem.id
    }
}