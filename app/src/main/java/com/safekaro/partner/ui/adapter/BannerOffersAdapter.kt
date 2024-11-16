package com.safekaro.partner.ui.adapter

import android.view.ViewGroup
import com.safekaro.partner.model.data.BannerData
import com.safekaro.partner.ui.common.BaseViewHolder
import com.github.islamkhsh.CardSliderAdapter
import com.safekaro.partner.R
import com.safekaro.partner.databinding.SliderBannerBinding

class BannerOffersAdapter constructor(
    private var itemList: ArrayList<BannerData>,
    private val onItemClicked: (BannerData) -> Unit = {}
) : CardSliderAdapter<BaseViewHolder<SliderBannerBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder.create(parent, SliderBannerBinding::inflate)

    override fun bindVH(holder: BaseViewHolder<SliderBannerBinding>, position: Int) {
        val data = itemList[position]
        holder.binding.ivImage.setImageResource(data.image ?: R.drawable.img_not_available)
        //holder.binding.ivImage.loadImage(data.offerBannerUrl ?: "")
        holder.itemView.setOnClickListener { onItemClicked(itemList[position]) }
        holder.itemView.tag = position
    }

    override fun getItemCount() = itemList.size

    fun updateList(list: ArrayList<BannerData>) {
        itemList = list
        notifyDataSetChanged()
    }

    fun updateItemAt(data: BannerData) {
        itemList.find { it.type == data.type }?.also {
            val index = itemList.indexOf(it)
            itemList[index] = data
            notifyItemChanged(index, data)
        }
    }

}