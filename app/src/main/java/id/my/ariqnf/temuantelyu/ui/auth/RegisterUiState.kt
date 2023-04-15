package id.my.ariqnf.temuantelyu.ui.auth

import id.my.ariqnf.temuantelyu.util.UiText

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val rePassword: String = "",
    val passwordVisible: Boolean = false,
    val fullName: String = ""
)

data class RegisterErrorState(
    val email: UiText = UiText.DynamicString(""),
    val password: UiText = UiText.DynamicString(""),
    val rePassword: UiText = UiText.DynamicString(""),
    val fullName: UiText = UiText.DynamicString(""),
)
