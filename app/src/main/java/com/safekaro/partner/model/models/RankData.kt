package com.safekaro.partner.model.models

import com.google.gson.annotations.SerializedName

data class RankData(
    override val status: String,
    override val message: String,
    val success: Boolean,
    val data: Rank? = null,
) : BaseResponse()

data class Rank (
    @SerializedName("partnerId"   ) var partnerId   : String? = null,
    @SerializedName("rank"        ) var rank        : String? = null,
    @SerializedName("policyCount" ) var policyCount : Int?    = null
)