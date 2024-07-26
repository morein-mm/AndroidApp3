package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post
import java.io.File

interface PostRepository {
    val data: Flow<PagingData<Post>>
//    val data: Flow<List<Post>>
    var startLocalId: Long
    suspend fun getAll()
    suspend fun getAllShown()
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, file: File)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long, likedByMe: Boolean)
    fun getNewerCount(newerId: Long): Flow<Int>
    suspend fun showAll()
}
