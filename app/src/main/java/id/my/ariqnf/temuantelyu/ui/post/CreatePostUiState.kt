package id.my.ariqnf.temuantelyu.ui.post

import android.net.Uri
import id.my.ariqnf.temuantelyu.util.UiText

data class CreatePostUiState(
    val title: String = "",
    val location: String = "",
    val description: String = "",
    val cate: String = "lost",
    val imageUri: Uri? = null,
    val tags: String = ""
)

data class CreatePostErrorState(
    val title: UiText = UiText.DynamicString(""),
    val location: UiText = UiText.DynamicString(""),
    val description: UiText = UiText.DynamicString(""),
    val tags: UiText = UiText.DynamicString("")
)
