package id.my.ariqnf.temuantelyu.ui.chat

import id.my.ariqnf.temuantelyu.data.Contact
import id.my.ariqnf.temuantelyu.util.UiText

data class ContactUiState(
    val data: List<Contact> = emptyList(),
    val error: UiText? = null
)
