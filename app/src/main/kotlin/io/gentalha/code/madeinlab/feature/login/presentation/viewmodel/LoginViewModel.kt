package io.gentalha.code.madeinlab.feature.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.gentalha.code.madeinlab.feature.login.domain.usecase.*
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginEvent
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()
    private val _eventChannel = Channel<LoginEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
        updateFormValidity()
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
        updateFormValidity()
    }

    fun onEmailFocusChanged() {
        val emailErrorResult = validateEmailUseCase(_uiState.value.email)
        _uiState.update { it.copy(emailError = emailErrorResult.errorMessage) }
    }

    fun onLoginClicked() {
        val emailResult = validateEmailUseCase(_uiState.value.email)
        val passwordResult = validatePasswordUseCase(_uiState.value.password)
        _uiState.update {
            it.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                _uiState.update { it.copy(isLoading = true) }
                loginUseCase(_uiState.value.email, _uiState.value.password)
                    .onSuccess {
                        _eventChannel.send(LoginEvent.LoginSuccess)
                    }
                    .onFailure { error ->
                        _eventChannel.send(LoginEvent.LoginFailure(error.message ?: "Erro desconhecido"))
                    }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateFormValidity() {
        val isEmailValid = validateEmailUseCase(_uiState.value.email).isSuccess
        val isPasswordValid = validatePasswordUseCase(_uiState.value.password).isSuccess
        _uiState.update { it.copy(isFormValid = isEmailValid && isPasswordValid) }
    }
}