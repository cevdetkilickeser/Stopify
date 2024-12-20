package com.cevdetkilickeser.stopify.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMLogin @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _loginState = MutableStateFlow(auth.currentUser != null)
    val loginState: StateFlow<Boolean> = _loginState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loadingState.value =  true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        _loginState.value = task.isSuccessful
                    }.addOnFailureListener { error ->
                        _errorState.value = error.message
                        _loadingState.value = false
                    }
            } catch (e: Exception) {
                _loginState.value = false
            }
        }
    }
}