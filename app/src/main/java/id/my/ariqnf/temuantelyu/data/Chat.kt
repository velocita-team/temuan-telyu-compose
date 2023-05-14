package id.my.ariqnf.temuantelyu.data

import com.google.firebase.Timestamp

data class Chat(
    val sender: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
