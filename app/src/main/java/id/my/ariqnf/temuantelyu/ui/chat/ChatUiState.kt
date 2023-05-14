package id.my.ariqnf.temuantelyu.ui.chat

import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.util.UiText

data class ChatUiState(
    val currentUserId: String = "",
    val otherUserName: String = "",
    val data: Map<String, List<Chat>> = emptyMap(),
    val error: UiText? = null
)
