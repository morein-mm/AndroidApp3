package ru.netology.nmedia.dto

import ru.netology.nmedia.enumeration.AttachmentType

data class Post(
    val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val draft: Boolean,
    val shown: Boolean,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
) {

}

data class PushToken(val token: String)

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class Media(val id: String)
