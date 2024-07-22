package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token
import ru.netology.nmedia.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
) : ViewModel() {
    private val repository = AuthRepository()
    val auth: LiveData<Token?> = appAuth.state
        .asLiveData()

    val isAuthorized: Boolean
        get() = auth.value?.token != null


    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                val token = repository.login(login, password)
                appAuth.setAuth(token.id, token.token)
            } catch (e: Exception) {
            }
        }
    }

    fun registerUser(login: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val token = repository.registerUser(login, password, name)
                appAuth.setAuth(token.id, token.token)
            } catch (e: Exception) {
            }
        }
    }

}