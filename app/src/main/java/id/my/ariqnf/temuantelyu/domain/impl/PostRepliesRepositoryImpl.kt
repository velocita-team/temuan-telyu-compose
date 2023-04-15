package id.my.ariqnf.temuantelyu.domain.impl

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.data.Reply
import id.my.ariqnf.temuantelyu.domain.PostRepliesRepository
import id.my.ariqnf.temuantelyu.util.DATE_FIELD
import id.my.ariqnf.temuantelyu.util.FULLNAME_FIELD
import id.my.ariqnf.temuantelyu.util.POSTS_COLL
import id.my.ariqnf.temuantelyu.util.REPLIES_COLL
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.USERS_COLL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOG_TAG = "PostRepliesRepo"

class PostRepliesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : PostRepliesRepository {

    override suspend fun getPost(userId: String, postId: String): Resource<Post> {
        return try {
            val docRef = firestore.collection(USERS_COLL).document(userId)
                .collection(POSTS_COLL).document(postId)
            val result = docRef.get().await().toObject(Post::class.java)
                ?.copy(
                    sender = firestore.collection(USERS_COLL).document(userId).get()
                        .await().data?.get(FULLNAME_FIELD) as String
                )
            Resource.Success(result)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get post", e)
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getReplies(
        userId: String,
        postId: String
    ): Resource<Flow<List<Reply>>> {
        return try {
            val docRef = firestore.collection(USERS_COLL).document(userId)
                .collection(POSTS_COLL).document(postId)
                .collection(REPLIES_COLL)
                .orderBy(DATE_FIELD, Query.Direction.DESCENDING)

            val result = docRef.snapshots().map {
                it.toObjects(Reply::class.java).map { reply ->
                    reply.copy(
                        sender = firestore.collection(USERS_COLL)
                            .document(reply.sender!!)
                            .get().await()
                            .data?.get(FULLNAME_FIELD) as String
                    )
                }
            }
            Resource.Success(result)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get replies", e)
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun sendReply(
        userId: String,
        postId: String,
        sender: String,
        content: String
    ): Resource<Unit> {
        var result: Resource<Unit> = Resource.Error("")
        firestore.collection(USERS_COLL).document(userId)
            .collection(POSTS_COLL).document(postId)
            .collection(REPLIES_COLL).document()
            .set(
                Reply(
                    sender,
                    content,
                    Timestamp.now()
                )
            ).addOnSuccessListener {
                Log.i(LOG_TAG, "Reply send successfully")
                result = Resource.Success(Unit)
            }
            .addOnFailureListener {
                Log.e(LOG_TAG, "Fail to send reply!", it)
                result = Resource.Error(it.message.toString())
            }

        return result
    }
}