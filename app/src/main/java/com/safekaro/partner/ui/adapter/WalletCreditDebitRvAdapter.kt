package com.safekaro.partner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.safekaro.partner.R
import com.safekaro.partner.databinding.ItemWalletCreditDebitBinding
import com.safekaro.partner.model.models.CreditDebit
import com.safekaro.partner.utils.formatDateTime


class WalletCreditDebitRvAdapter(
    private val onViewClicked: (item: CreditDebit) -> Unit = {}
) : ListAdapter<CreditDebit, WalletCreditDebitRvAdapter.ViewHolder>(CreditDebitDiffUtilCallback()) {

    class ViewHolder(val binding: ItemWalletCreditDebitBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWalletCreditDebitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            tvDate.text = formatDateTime(item.createdOn)
            tvParticular.text = item.accountType
            tvRemark.text = item.remarks
            tvTransactionsCode.text = item.transactionCode
            tvBalance.text = holder.itemView.context.getString(R.string.ruppe_symbol_placeholder, item.partnerBalance.toString())
            tvCredit.text = holder.itemView.context.getString(R.string.ruppe_symbol_credit, item.credit.toString())
            tvDebit.text = holder.itemView.context.getString(R.string.ruppe_symbol_debit, item.debit.toString())
            layRemark.isVisible = item.remarks.isNullOrBlank().not()
            layView.isVisible = (item.credit ?: 0) > 0
            btnView.setOnClickListener { onViewClicked(getItem(position)) }
        }
    }

    fun getItemAt(position: Int): CreditDebit {
        return getItem(position)
    }

    fun updateList(list: ArrayList<CreditDebit>) {
        submitList(list.toMutableList())
    }

    fun appendList(list: ArrayList<CreditDebit>) {
        val newList = currentList.toMutableList()
        newList.addAll(list)
        submitList(newList)
    }

}