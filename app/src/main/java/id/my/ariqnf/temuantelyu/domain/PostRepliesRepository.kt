package id.my.ariqnf.temuantelyu.domain

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.data.Reply
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.flow.Flow

interface PostRepliesRepository {
    suspend fun getPost(userId: String, postId: String): Resource<Post>
    suspend fun getReplies(userId: String, postId: String): Resource<Flow<List<Reply>>>
    suspend fun sendReply(
        userId: String,
        postId: String,
        sender: String,
        content: String
    ): Resource<Unit>
}