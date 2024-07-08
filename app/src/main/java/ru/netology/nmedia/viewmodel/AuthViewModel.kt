package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    val auth: LiveData<Token?> = AppAuth.getInstance().state
        .asLiveData()

    val isAuthorized: Boolean
        get() = auth.value?.token != null


    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repository.login(login, password)
                AppAuth.getInstance().setAuth(token.id, token.token)
            } catch (e: Exception) {
                println("11")
//                _dataState.value = FeedModelState(error = true)
                //как обработать ошибку?


            }
        }
    }

}