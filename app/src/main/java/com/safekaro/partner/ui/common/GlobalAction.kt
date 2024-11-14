package com.safekaro.partner.ui.common

sealed class GlobalAction<out T : Any> {
    data object WalletClick : GlobalAction<Unit>()
    data object NotificationClick : GlobalAction<Unit>()
    data object ProfileClick : GlobalAction<Unit>()
}

sealed class UiAction<out T : Any> {
    data object Ideal : UiAction<Nothing>()
    data class UpdateWallet(val data: Any) : UiAction<Any>()
}