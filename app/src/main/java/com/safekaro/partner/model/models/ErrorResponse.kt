package com.safekaro.partner.model.models

data class ErrorResponse(
    val message: String,
    val status: String,
    val err: String,
    val level: String,
    val timestamp: String
)