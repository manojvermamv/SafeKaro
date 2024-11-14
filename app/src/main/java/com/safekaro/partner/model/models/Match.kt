package com.safekaro.partner.model.models

import com.google.gson.annotations.SerializedName

data class Match(
  @SerializedName("id"              ) var id             : String?                = null,
  @SerializedName("name"            ) var name           : String?                = null,
  @SerializedName("match_format"    ) var matchFormat    : String?                = null,
  @SerializedName("tournament_name" ) var tournamentName : String?                = null,
  @SerializedName("status"          ) var status         : String?                = null,
  @SerializedName("starts_at"       ) var startsAt       : String?                = null,
)