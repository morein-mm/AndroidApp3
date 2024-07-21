package ru.netology.nmedia.repository

import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError

class AuthRepository() {
//    override val data = dao.getAllShown()
//        .map(List<PostEntity>::toDto)
//        .flowOn(Dispatchers.Default)

// какая должна быть лайфдата?

    suspend fun login(login: String, pass: String): Token {
        try {
            val response = AuthApi.service.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            return body
//            Как передать токен?
//            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    suspend fun registerUser(login: String, pass: String, name: String): Token {
        try {
            val response = AuthApi.service.registerUser(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}
