package id.my.ariqnf.temuantelyu.data

import com.google.firebase.Timestamp

data class Reply(
    val sender: String? = "",
    val content: String? = "",
    val date: Timestamp? = null,
)
