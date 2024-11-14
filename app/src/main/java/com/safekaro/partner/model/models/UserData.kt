package com.safekaro.partner.model.models

import com.google.gson.annotations.SerializedName


data class UserData (
  @SerializedName("status"       ) var status       : String,
  @SerializedName("message"      ) var message      : String,
  @SerializedName("accessToken"  ) var accessToken  : String? = null,
  @SerializedName("refreshToken" ) var refreshToken : String? = null,
  @SerializedName("name"         ) var name         : String? = null,
  @SerializedName("email"        ) var email        : String? = null,
  @SerializedName("role"         ) var role         : String? = null,
  @SerializedName("partnerId"    ) var partnerId    : String? = null,
  @SerializedName("partnerCode"  ) var partnerCode  : String? = null,
  @SerializedName("phoneNumber"  ) var phoneNumber  : Long?   = null,
  @SerializedName("id"           ) var id           : String? = null
)