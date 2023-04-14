package id.my.ariqnf.temuantelyu.ui.profile

import id.my.ariqnf.temuantelyu.data.User
import id.my.ariqnf.temuantelyu.util.UiText

data class ProfileUiState(
    val user: User,
    val error: UiText? = null
)
