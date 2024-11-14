package com.safekaro.partner.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.safekaro.partner.databinding.ItemUpcomingMatchBinding
import com.safekaro.partner.model.models.Match
import com.safekaro.partner.ui.common.BaseViewHolder
import com.safekaro.partner.utils.formatDateTime
import com.safekaro.partner.utils.formatTimeRemaining


class UpcomingMatchesRvAdapter(
    private val onItemClicked: (position: Int, item: Match) -> Unit = {_, _ -> }
) : ListAdapter<Match, BaseViewHolder<ItemUpcomingMatchBinding>>(MatchDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder.create(parent, ItemUpcomingMatchBinding::inflate)

    override fun onBindViewHolder(holder: BaseViewHolder<ItemUpcomingMatchBinding>, position: Int) {
        holder.binding.apply {
            val item = getItem(position)
            tvMatchFormat.text = item.matchFormat
            tvTournamentName.text = item.tournamentName
            //layLineupsOut.isVisible = item.metadata?.isLineupOut ?: false

            val duration = (item.startsAt ?: "0").toLong()
            tvTimer.text = formatTimeRemaining(duration)
            tvDate.text = formatDateTime(duration)

            //bgTeamALogo.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))

            //layOffer.isVisible = item.matchOffers.isNotEmpty()
            //item.matchOffers.getOrNull(0)?.let {
            //    tvOfferTitle.text = it.title
            //    tvOfferSubText.text = it.subtext
            //    ivOfferIcon.loadImage(it.offerIconUrl)
            //}
        }

        holder.itemView.setOnClickListener { onItemClicked(position, getItem(position)) }
    }

    fun getItemAt(position: Int): Match {
        return getItem(position)
    }

    fun updateList(list: ArrayList<Match>) {
        submitList(list.toMutableList())
    }

    fun appendList(list: ArrayList<Match>) {
        val newList = currentList.toMutableList()
        newList.addAll(list)
        submitList(newList)
    }

}