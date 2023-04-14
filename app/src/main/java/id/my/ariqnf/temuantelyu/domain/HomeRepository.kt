package id.my.ariqnf.temuantelyu.domain

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getAllPost(): Resource<Flow<List<Post>>>
    suspend fun getUserName(documentId: String): Resource<String?>
}