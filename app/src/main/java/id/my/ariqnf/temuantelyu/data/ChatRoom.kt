package id.my.ariqnf.temuantelyu.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatRoom(
    val roomId: String = "",
    val otherUserName: String = "",
    val result: Flow<List<Chat>> = emptyFlow()
)
