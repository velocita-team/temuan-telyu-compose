package id.my.ariqnf.temuantelyu.domain

import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.data.ChatRoom
import id.my.ariqnf.temuantelyu.data.Contact
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(userId: String, otherUserId: String): Resource<ChatRoom>

    suspend fun sendReply(docId: String, chat: Chat): Resource<Unit>

    suspend fun getMyChats(userID: String): Resource<Flow<List<Contact>>>
}