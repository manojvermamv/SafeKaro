package com.safekaro.partner.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.safekaro.partner.R
import com.safekaro.partner.databinding.SliderUserMatchesBinding
import com.safekaro.partner.model.models.UserMatchesList
import com.safekaro.partner.ui.common.BaseViewHolder
import com.safekaro.partner.utils.setBackgroundTintByRes
import com.safekaro.partner.utils.setTextColorByRes
import com.github.islamkhsh.CardSliderAdapter

class UserMatchesAdapter constructor(
    private val context: Context,
    private var itemList: ArrayList<UserMatchesList>,
    private val onItemClicked: (UserMatchesList) -> Unit = {}
) : CardSliderAdapter<BaseViewHolder<SliderUserMatchesBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder.create(parent, SliderUserMatchesBinding::inflate)

    override fun bindVH(holder: BaseViewHolder<SliderUserMatchesBinding>, position: Int) {
        val data = itemList[position]

        holder.binding.apply {
            tvJoining.text = String.format(context.getString(R.string.count_placeholder), data.userTeams ?: 0)
            tvContest.text = String.format(context.getString(R.string.count_placeholder), data.userContests ?: 0)
            tvTopRunningRank.text = (data.topRunningRank ?: 0).toString()

            val item = data.match ?: return
            tvMatchFormat.text = item.matchFormat
            tvTournamentName.text = item.tournamentName

            val (status, color) = when ((item.status ?: "").lowercase()) {
                "started", "live" -> Pair(R.string.live, R.color.status_live)
                "completed" -> Pair(R.string.completed, R.color.status_completed)
                "upcoming" -> Pair(R.string.upcoming, R.color.status_upcoming)
                else -> Pair(R.string.not_started, R.color.dark_grey)
            }
            tvStatus.text = context.getString(status)
            tvStatus.setTextColorByRes(color)
            tvStatus.setBackgroundTintByRes(color)

            /*item.teams?.a?.let {
                tvTeamACode.text = it.code
                bgTeamALogo.isVisible = it.logoBgColor != null
                it.logoBgColor?.let { color ->
                    bgTeamALogo.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
                }
            }
            item.teams?.b?.let {
                tvTeamBCode.text = it.code
                bgTeamBLogo.isVisible = it.logoBgColor != null
                it.logoBgColor?.let { color ->
                    bgTeamBLogo.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
                }
            }*/
        }

        holder.itemView.setOnClickListener { onItemClicked(itemList[position]) }
        holder.itemView.tag = position
    }

    override fun getItemCount() = itemList.size

    fun updateList(list: ArrayList<UserMatchesList>) {
        itemList = list
        notifyDataSetChanged()
    }

    fun updateItemAt(data: UserMatchesList) {
        itemList.find { it.match?.id == data.match?.id }?.also {
            val index = itemList.indexOf(it)
            itemList[index] = data
            notifyItemChanged(index, data)
        }
    }

}