package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val draft: Boolean,
    val shown: Boolean= true
) {
    fun toDto() = Post(id, author, authorAvatar, content, published, likedByMe, likes, draft, shown)

    companion object {
        fun fromDto(dto: Post, shown: Boolean = true ) =
            PostEntity(dto.id, dto.author, dto.authorAvatar, dto.content, dto.published, dto.likedByMe, dto.likes, dto.draft, shown)

    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(shown: Boolean = true): List<PostEntity> = map { PostEntity.fromDto(it, shown) }
