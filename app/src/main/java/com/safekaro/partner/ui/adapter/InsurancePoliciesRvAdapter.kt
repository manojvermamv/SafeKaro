package com.safekaro.partner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safekaro.partner.databinding.ItemInsurancePolicyBinding
import com.safekaro.partner.model.data.InsurancePolicy
import com.safekaro.partner.ui.adapter.InsurancePoliciesRvAdapter.ViewHolder


class InsurancePoliciesRvAdapter(
    private val onItemClicked: (item: InsurancePolicy) -> Unit = {}
) : ListAdapter<InsurancePolicy, ViewHolder>(InsurancePolicyDiffUtilCallback()) {

    class ViewHolder(val binding: ItemInsurancePolicyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemInsurancePolicyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            title.text = item.title
            icon.setImageResource(item.icon)
        }

        holder.itemView.setOnClickListener { onItemClicked(getItem(position)) }
    }

    fun getItemAt(position: Int): InsurancePolicy {
        return getItem(position)
    }

    fun updateList(list: ArrayList<InsurancePolicy>) {
        submitList(list.toMutableList())
    }

    fun appendList(list: ArrayList<InsurancePolicy>) {
        val newList = currentList.toMutableList()
        newList.addAll(list)
        submitList(newList)
    }

}