package com.cognitech.mindflow.ui.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cognitech.mindflow.application.AuthUseCases
import kotlinx.coroutines.launch

data class AuthFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val loading: Boolean = false,
)

class AuthViewModel(private val auth: AuthUseCases) : ViewModel() {

    var state by mutableStateOf(AuthFormState())
        private set

    fun onNameChange(value: String) { state = state.copy(name = value, nameError = null, generalError = null) }
    // El teclado puede insertar espacios o caracteres invisibles al autocompletar; un correo nunca los lleva.
    fun onEmailChange(value: String) {
        val clean = value.filterNot { it.isWhitespace() || it in INVISIBLE_CHARS }
        state = state.copy(email = clean, emailError = null, generalError = null)
    }
    fun onPasswordChange(value: String) { state = state.copy(password = value, passwordError = null, generalError = null) }

    fun reset() { state = AuthFormState() }

    fun signIn(onSuccess: () -> Unit) {
        val emailError = validateEmail(state.email)
        val passwordError = if (state.password.isBlank()) "Ingresa tu contraseña" else null
        if (emailError != null || passwordError != null) {
            state = state.copy(emailError = emailError, passwordError = passwordError)
            return
        }
        submit(onSuccess) { auth.signIn(state.email, state.password) }
    }

    fun signUp(onSuccess: () -> Unit) {
        val nameError = if (state.name.trim().length < 2) "Ingresa tu nombre completo" else null
        val emailError = validateEmail(state.email)
        val passwordError = if (state.password.length < 8) "Mínimo 8 caracteres" else null
        if (nameError != null || emailError != null || passwordError != null) {
            state = state.copy(nameError = nameError, emailError = emailError, passwordError = passwordError)
            return
        }
        submit(onSuccess) { auth.signUp(state.name, state.email, state.password) }
    }

    private fun submit(onSuccess: () -> Unit, action: suspend () -> Result<*>) {
        state = state.copy(loading = true, generalError = null)
        viewModelScope.launch {
            action()
                .onSuccess {
                    state = AuthFormState()
                    onSuccess()
                }
                .onFailure { state = state.copy(loading = false, generalError = it.message) }
        }
    }

    private companion object {
        val INVISIBLE_CHARS = setOf('​', '‌', '‍', '⁠', '﻿', '­')
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Ingresa tu correo electrónico"
        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Correo electrónico no válido"
        else -> null
    }
}
