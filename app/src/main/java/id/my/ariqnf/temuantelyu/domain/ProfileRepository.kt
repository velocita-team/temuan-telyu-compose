package id.my.ariqnf.temuantelyu.domain

import id.my.ariqnf.temuantelyu.util.Resource

interface ProfileRepository {
    suspend fun getUserName(documentId: String): Resource<String?>
}