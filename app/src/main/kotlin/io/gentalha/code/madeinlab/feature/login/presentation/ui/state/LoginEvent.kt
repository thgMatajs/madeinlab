package io.gentalha.code.madeinlab.feature.login.presentation.ui.state

sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
    data class LoginFailure(val message: String) : LoginEvent()
}