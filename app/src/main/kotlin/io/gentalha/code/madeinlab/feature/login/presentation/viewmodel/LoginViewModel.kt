package io.gentalha.code.madeinlab.feature.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gentalha.code.madeinlab.core.validation.Rules
import io.gentalha.code.madeinlab.feature.login.domain.repository.LoginRepository
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginEvent
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    private val _eventChannel = Channel<LoginEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val emailRules = listOf(
        Rules.NotEmptyRule("O e-mail não pode ser vazio."),
        Rules.EmailRule("O formato do e-mail é inválido.")
    )
    private val passwordRules = listOf(
        Rules.NotEmptyRule("A senha não pode ser vazia."),
        Rules.MinLengthRule(6, "A senha deve ter no mínimo 6 caracteres.")
    )

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
        updateFormValidity()
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
        updateFormValidity()
    }

    fun onEmailFocusChanged() {
        val emailErrorResult = validateEmail()
        _uiState.update { it.copy(emailError = emailErrorResult) }
    }

    fun onLoginClicked() {
        val emailErrorResult = validateEmail()
        val passwordErrorResult = validatePassword()
        _uiState.update {
            it.copy(
                emailError = emailErrorResult,
                passwordError = passwordErrorResult
            )
        }

        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                _uiState.update { it.copy(isLoading = true) }
                repository.login(_uiState.value.email, _uiState.value.password)
                    .onSuccess {
                        _eventChannel.send(LoginEvent.LoginSuccess)
                    }
                    .onFailure { error ->
                        _eventChannel.send(LoginEvent.LoginFailure(error.message ?: "Erro desconhecido"))
                    }
            } finally {
                // Este bloco é EXECUTADO SEMPRE, garantindo que o spinner suma.
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validateEmail(): String? {
        return emailRules.firstOrNull { !it.validate(_uiState.value.email) }?.errorMessage
    }

    private fun validatePassword(): String? {
        return passwordRules.firstOrNull { !it.validate(_uiState.value.password) }?.errorMessage
    }

    private fun updateFormValidity() {
        val isFormValid = validateEmail() == null && validatePassword() == null
        _uiState.update { it.copy(isFormValid = isFormValid) }
    }
}