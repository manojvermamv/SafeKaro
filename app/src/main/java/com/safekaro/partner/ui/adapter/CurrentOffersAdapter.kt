package com.safekaro.partner.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.safekaro.partner.databinding.SliderCurrentOffersBinding
import com.safekaro.partner.model.models.OfferList
import com.safekaro.partner.ui.common.BaseViewHolder
import com.safekaro.partner.utils.loadImage
import com.github.islamkhsh.CardSliderAdapter

class CurrentOffersAdapter constructor(
    private var itemList: ArrayList<OfferList>,
    private val onItemClicked: (OfferList) -> Unit = {}
) : CardSliderAdapter<BaseViewHolder<SliderCurrentOffersBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder.create(parent, SliderCurrentOffersBinding::inflate)

    override fun bindVH(holder: BaseViewHolder<SliderCurrentOffersBinding>, position: Int) {
        val data = itemList[position]
        holder.binding.ivImage.loadImage(data.offerBannerUrl ?: "")
        holder.itemView.setOnClickListener { onItemClicked(itemList[position]) }
        holder.itemView.tag = position
    }

    override fun getItemCount() = itemList.size

    fun updateList(list: ArrayList<OfferList>) {
        itemList = list
        notifyDataSetChanged()
    }

    fun updateItemAt(data: OfferList) {
        itemList.find { it.type == data.type }?.also {
            val index = itemList.indexOf(it)
            itemList[index] = data
            notifyItemChanged(index, data)
        }
    }

}