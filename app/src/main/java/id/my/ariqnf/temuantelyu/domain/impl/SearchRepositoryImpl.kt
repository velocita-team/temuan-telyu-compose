package id.my.ariqnf.temuantelyu.domain.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.domain.SearchRepository
import id.my.ariqnf.temuantelyu.util.DATE_FIELD
import id.my.ariqnf.temuantelyu.util.DONE_FIELD
import id.my.ariqnf.temuantelyu.util.FULLNAME_FIELD
import id.my.ariqnf.temuantelyu.util.IMAGE_URL_FIELD
import id.my.ariqnf.temuantelyu.util.POSTS_COLL
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.SENDER_FIELD
import id.my.ariqnf.temuantelyu.util.TAGS_FIELD
import id.my.ariqnf.temuantelyu.util.USERS_COLL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOG_TAG = "SearchRepo"

class SearchRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    SearchRepository {
    override suspend fun getAllPostsWhereTags(tags: List<String>): Resource<Flow<List<Post>>> {
        return try {
            val result =
                firestore.collectionGroup(POSTS_COLL)
                    .whereEqualTo(DONE_FIELD, false)
                    .whereArrayContainsAny(TAGS_FIELD, tags)
                    .orderBy(DATE_FIELD, Query.Direction.DESCENDING)
                    .snapshots()
                    .map { documents ->
                        val list = mutableListOf<Post>()
                        for (document in documents) {
                            val senderReference = document.data[SENDER_FIELD] as String
                            val sender =
                                firestore.collection(USERS_COLL)
                                    .document(senderReference)
                                    .get().await().data
                            val image =
                                if (document.data[IMAGE_URL_FIELD] == null) "" else document.data[IMAGE_URL_FIELD] as String

                            list.add(
                                document.toObject(Post::class.java).copy(
                                    id = document.reference.path,
                                    imageUrl = image,
                                    sender = sender?.get(FULLNAME_FIELD) as String
                                )
                            )
                        }
                        return@map list
                    }
            Log.i(LOG_TAG, "Get posts success")
            Resource.Success(result)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get post", e)
            Resource.Error(e.message.toString())
        }
    }
}