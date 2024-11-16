package com.safekaro.partner.model.data

import androidx.annotation.DrawableRes

data class PerformanceData(
    val title: String,
    val count: Long,
    @DrawableRes val icon: Int
)