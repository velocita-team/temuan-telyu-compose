package id.my.ariqnf.temuantelyu.domain

import android.net.Uri
import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun uploadPost(
        userId: String,
        title: String,
        location: String,
        description: String,
        cate: String,
        tags: List<String>,
        imageUri: Uri?
    ): Resource<Unit>

    suspend fun getMyPosts(userId: String): Resource<Flow<List<Post>>>
    suspend fun deletePost(userId: String, documentId: String): Resource<Unit>
}