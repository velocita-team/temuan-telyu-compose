package id.my.ariqnf.temuantelyu.ui.search

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.util.UiText

data class SearchUiState(
    val data: List<Post> = emptyList(),
    val cateFilter: String = "",
    val error: UiText? = null
)