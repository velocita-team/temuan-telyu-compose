package id.my.ariqnf.temuantelyu.ui.post

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.data.Reply
import id.my.ariqnf.temuantelyu.util.UiText

sealed interface PostRepliesUiState {
    val error: UiText?

    data class PostState(
        val post: Post = Post(),
        override val error: UiText? = null
    ) : PostRepliesUiState

    data class RepliesState(
        val replies: List<Reply> = emptyList(),
        override val error: UiText? = null
    ) : PostRepliesUiState
}
