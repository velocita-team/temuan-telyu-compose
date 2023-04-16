package id.my.ariqnf.temuantelyu.domain

import id.my.ariqnf.temuantelyu.data.Post
import id.my.ariqnf.temuantelyu.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getAllPostsWhereTags(tags: List<String>): Resource<Flow<List<Post>>>
}