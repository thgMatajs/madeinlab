package io.gentalha.code.madeinlab.feature.login.presentation.ui.state

data class LoginUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isFormValid: Boolean = false,
    val loginSuccess: Boolean = false
)