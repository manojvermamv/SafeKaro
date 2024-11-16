package com.safekaro.partner.model.models

abstract class BaseResponse {
    abstract val status: String
    abstract val message: String
}