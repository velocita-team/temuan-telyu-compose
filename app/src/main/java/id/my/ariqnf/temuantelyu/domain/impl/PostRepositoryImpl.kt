package id.my.ariqnf.temuantelyu.domain.impl

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.domain.PostRepository
import id.my.ariqnf.temuantelyu.util.DATE_FIELD
import id.my.ariqnf.temuantelyu.util.DONE_FIELD
import id.my.ariqnf.temuantelyu.util.POSTS_COLL
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.USERS_COLL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOG_TAG = "PostRepo"

class PostRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) : PostRepository {
    override suspend fun uploadPost(
        userId: String,
        title: String,
        location: String,
        description: String,
        cate: String,
        tags: List<String>,
        imageUri: Uri?,
    ): Resource<Unit> {
        return try {
//      Upload image to cloud storage
            val storageRef = storage.reference
                .child("postImg")
                .child("$userId-${System.currentTimeMillis()}")
            var newPost = Post(
                title = title,
                location = location,
                content = description,
                sender = userId,
                date = Timestamp.now(),
                tags = tags,
                cate = cate,
                imageUrl = null,
            )
            imageUri?.let {
                newPost = newPost.copy(
                    imageUrl = storageRef.putFile(it).await()
                        .storage.downloadUrl.await().toString()
                )
            }

            firestore.collection(USERS_COLL).document(userId)
                .collection(POSTS_COLL).document().set(newPost).await()
            Log.i(LOG_TAG, "Post uploaded")

            Resource.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Post fail to upload", e)
            Resource.Error(message = e.message.toString())
        }
    }

    override suspend fun getMyPosts(userId: String): Resource<Flow<List<Post>>> {
        return try {
            val result = firestore.collection(USERS_COLL).document(userId)
                .collection(POSTS_COLL)
                .whereEqualTo(DONE_FIELD, false)
                .orderBy(DATE_FIELD).snapshots()
                .map { querySnapshot ->
                    val list = mutableListOf<Post>()
                    for (query in querySnapshot) {
                        list.add(
                            query.toObject(Post::class.java).copy(
                                id = query.id
                            )
                        )
                    }
                    return@map list
                }

            Resource.Success(result)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get my post", e)
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deletePost(
        userId: String,
        documentId: String
    ): Resource<Unit> {
        return try {
            firestore.collection(USERS_COLL).document(userId)
                .collection(POSTS_COLL).document(documentId)
                .update(DONE_FIELD, true).await()
            Resource.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail To delete", e)
            Resource.Error(message = e.message.toString())
        }
    }
}