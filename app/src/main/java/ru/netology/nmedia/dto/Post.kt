package ru.netology.nmedia.dto

import ru.netology.nmedia.enumeration.AttachmentType

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val draft: Boolean,
    val shown: Boolean,
    val attachment: Attachment? = null
) {

}

data class Attachment(
    val url: String,
    val type: AttachmentType
)

data class Media(val id: String)
