package id.my.ariqnf.temuantelyu.domain.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import id.my.ariqnf.temuantelyu.data.Chat
import id.my.ariqnf.temuantelyu.data.ChatRoom
import id.my.ariqnf.temuantelyu.data.Contact
import id.my.ariqnf.temuantelyu.domain.ChatRepository
import id.my.ariqnf.temuantelyu.util.CHAT_COLL
import id.my.ariqnf.temuantelyu.util.CHAT_ROOM_COLL
import id.my.ariqnf.temuantelyu.util.COMPOUND_FIELD
import id.my.ariqnf.temuantelyu.util.FULLNAME_FIELD
import id.my.ariqnf.temuantelyu.util.MEMBER1_FIELD
import id.my.ariqnf.temuantelyu.util.MEMBER2_FIELD
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.TIMESTAMP_FIELD
import id.my.ariqnf.temuantelyu.util.USERS_COLL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOG_TAG = "ChatRepo"

class ChatRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    ChatRepository {

    override suspend fun getChats(
        userId: String,
        otherUserId: String
    ): Resource<ChatRoom> {
        return try {
            // Get correct chat room document id
            var docRefId = firestore.collection(CHAT_ROOM_COLL)
                .whereIn(MEMBER1_FIELD, listOf(userId, otherUserId))
                .whereIn(MEMBER2_FIELD, listOf(userId, otherUserId))
                .get().await().documents
                .firstOrNull()?.id

            // If docRefId null create new document
            if (docRefId == null) {
                val newChatRoom = firestore.collection(CHAT_ROOM_COLL).document()
                newChatRoom.set(
                    mapOf(
                        MEMBER1_FIELD to userId,
                        MEMBER2_FIELD to otherUserId,
                        COMPOUND_FIELD to listOf(userId, otherUserId)
                    )
                ).await()

                docRefId = newChatRoom.id
            }

            // Get interlocutor/conversational partner name
            val otherUserName = firestore.collection(USERS_COLL)
                .document(otherUserId).get().await()
                .get(FULLNAME_FIELD) as String

            val result =
                firestore.collection(CHAT_ROOM_COLL).document(docRefId).collection(CHAT_COLL)
                    .orderBy(TIMESTAMP_FIELD, Query.Direction.ASCENDING)
                    .snapshots()
                    .map {
                        it.toObjects(Chat::class.java)
                    }

            Resource.Success(
                ChatRoom(
                    docRefId,
                    otherUserName,
                    result
                )
            )
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get chat", e)
            Resource.Error(e.message.toString())
        } catch (e: NoSuchElementException) {
            Log.e(LOG_TAG, "Doc ref empty", e)
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun sendReply(docId: String, chat: Chat): Resource<Unit> {
        return try {
            firestore.collection(CHAT_ROOM_COLL).document(docId)
                .collection(CHAT_COLL).document()
                .set(chat).await()

            Log.i(LOG_TAG, "Chat uploaded")
            Resource.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to post chat", e)
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getMyChats(userID: String): Resource<Flow<List<Contact>>> {
        return try {
            val docRef = firestore.collection(CHAT_ROOM_COLL)
            val result = docRef.whereArrayContains(COMPOUND_FIELD, userID)
                .snapshots()
                .map { query ->
                    val list = mutableListOf<Contact>()
                    for (document in query) {
                        val latestChat = docRef.document(document.id).collection(CHAT_COLL)
                            .orderBy(TIMESTAMP_FIELD, Query.Direction.DESCENDING).limit(1).get()
                            .await()
                        // Get partner id
                        val partner = if (userID == document.get(MEMBER1_FIELD))
                            document.get(MEMBER2_FIELD) as String
                        else
                            document.get(MEMBER1_FIELD) as String

                        val partnerName = firestore.collection(USERS_COLL)
                            .document(partner)
                            .get().await().data?.get(FULLNAME_FIELD) as String

                        if (latestChat.size() > 0) {
                            list.add(
                                Contact(
                                    partner,
                                    partnerName,
                                    latestChat.toObjects(Chat::class.java).first()
                                )
                            )
                        }
                    }
                    list
                }

            Resource.Success(result)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get chats", e)
            Resource.Error(e.message.toString())
        }
    }
}