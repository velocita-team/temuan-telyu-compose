package id.my.ariqnf.temuantelyu.domain.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import id.my.ariqnf.temuantelyu.domain.ProfileRepository
import id.my.ariqnf.temuantelyu.util.FULLNAME_FIELD
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.USERS_COLL
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOG_TAG = "ProfileRepo"

class ProfileRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    ProfileRepository {
    override suspend fun getUserName(documentId: String): Resource<String?> {
        return try {
            val userName =
                firestore.collection(USERS_COLL).document(documentId).get().await().data
            Resource.Success(userName?.get(FULLNAME_FIELD) as String?)
        } catch (e: FirebaseFirestoreException) {
            Log.e(LOG_TAG, "Fail to get full name", e)
            Resource.Error(e.message.toString())
        }
    }
}