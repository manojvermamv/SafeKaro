package com.safekaro.partner.model.data

import com.google.gson.annotations.SerializedName


data class BannerData (
  @SerializedName("offer_banner_url" ) var offerBannerUrl : String? = null,
  @SerializedName("type"             ) var type           : String? = null,
  @SerializedName("image"            ) var image          : Int? = null
)