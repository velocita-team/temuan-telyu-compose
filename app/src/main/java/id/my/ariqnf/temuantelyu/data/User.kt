package id.my.ariqnf.temuantelyu.data

import com.google.firebase.firestore.Exclude

data class User(
    val fullName: String? = "",
    @get:Exclude val email: String? = "" // exclude property add to Firestore
)
