package id.my.ariqnf.temuantelyu.domain

import com.google.firebase.auth.AuthResult
import id.my.ariqnf.temuantelyu.util.Resource

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Resource<AuthResult>
    suspend fun registerUser(email: String, password: String): Resource<AuthResult>
    suspend fun setUserFullName(userId: String, fullName: String): Resource<Unit>
    suspend fun createAnonymous(): Resource<Unit>
}