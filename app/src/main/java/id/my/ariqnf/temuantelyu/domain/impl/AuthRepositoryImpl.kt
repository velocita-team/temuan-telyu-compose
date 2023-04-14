package id.my.ariqnf.temuantelyu.domain.impl

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import id.my.ariqnf.temuantelyu.data.User
import id.my.ariqnf.temuantelyu.domain.AuthRepository
import id.my.ariqnf.temuantelyu.util.Resource
import id.my.ariqnf.temuantelyu.util.USERS_COLL
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val LOG_TAG = "AuthRepo"

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun loginUser(email: String, password: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        } catch (e: FirebaseAuthException) {
            Log.e(LOG_TAG, "Fail to login", e)
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun registerUser(email: String, password: String): Resource<AuthResult> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        } catch (e: FirebaseAuthException) {
            Log.e(LOG_TAG, "Fail to register", e)
            Resource.Error(message = e.message.toString())
        }
    }

    override suspend fun setUserFullName(userId: String, fullName: String): Resource<Unit> {
        val newUser = User(fullName)
        var result: Resource<Unit> = Resource.Error("")
        firestore.collection(USERS_COLL).document(userId).set(newUser)
            .addOnSuccessListener {
                Log.i(LOG_TAG, "Full name set successfully!")
                result = Resource.Success(Unit)
            }
            .addOnFailureListener {
                Log.e(LOG_TAG, "Fail to set full name", it)
                result = Resource.Error(it.message.toString())
            }.await()

        return result
    }

    override suspend fun createAnonymous(): Resource<Unit> {
        var result: Resource<Unit> = Resource.Error("")
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                Log.i(LOG_TAG, "login anonymously success")
                result = Resource.Success(Unit)
            }
            .addOnFailureListener {
                Log.e(LOG_TAG, "Login anonymously failure", it)
                result = Resource.Error(it.message.toString())
            }

        return result
    }
}