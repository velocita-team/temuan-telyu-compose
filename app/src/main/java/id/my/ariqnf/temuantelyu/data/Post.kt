package id.my.ariqnf.temuantelyu.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class Post(
    @get:Exclude val id: String? = "",
    val title: String? = "",
    val sender: String? = "",
    val date: Timestamp? = null,
    val content: String? = "",
    val cate: String? = "",
    val tags: List<String>? = emptyList(),
    val imageUrl: String? = "",
    val isDone: Boolean? = false
)
