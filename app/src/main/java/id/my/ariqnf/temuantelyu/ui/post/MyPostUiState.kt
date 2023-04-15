package id.my.ariqnf.temuantelyu.ui.post

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.util.UiText

data class MyPostUiState(
    val data: List<Post> = emptyList(),
    val error: UiText? = null
)
