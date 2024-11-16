package com.safekaro.partner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ItemMyPerformanceBinding
import com.safekaro.partner.model.data.PerformanceData


class PerformanceRvAdapter(
    private val onItemClicked: (item: PerformanceData) -> Unit = {}
) : ListAdapter<PerformanceData, PerformanceRvAdapter.ViewHolder>(PerformanceDataDiffUtilCallback()) {

    class ViewHolder(val binding: ItemMyPerformanceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyPerformanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val padding = parent.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)
        val lp = binding.root.layoutParams
        lp.width = Math.round((parent.measuredWidth / 3).minus(padding.toFloat()))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            title.text = item.title
            value.text = holder.itemView.context.getString(R.string.ruppe_symbol_placeholder, item.count.toString())
            //icon.setImageResource(item.icon)
        }
        holder.itemView.setOnClickListener { onItemClicked(getItem(position)) }
    }

    fun getItemAt(position: Int): PerformanceData {
        return getItem(position)
    }

    fun updateList(list: ArrayList<PerformanceData>) {
        submitList(list.toMutableList())
    }

    fun appendList(list: ArrayList<PerformanceData>) {
        val newList = currentList.toMutableList()
        newList.addAll(list)
        submitList(newList)
    }

}