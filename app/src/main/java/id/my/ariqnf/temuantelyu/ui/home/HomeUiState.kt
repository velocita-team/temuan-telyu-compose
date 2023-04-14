package id.my.ariqnf.temuantelyu.ui.home

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.util.UiText

interface HomeUiState {
    val error: UiText?

    data class Posts(
        val data: List<Post> = emptyList(),
        override val error: UiText? = null
    ) : HomeUiState

    data class User(
        val name: String? = "Telyutizen",
        override val error: UiText? = null
    ) : HomeUiState
}