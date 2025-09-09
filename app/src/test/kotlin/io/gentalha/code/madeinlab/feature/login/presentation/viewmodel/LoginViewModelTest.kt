package io.gentalha.code.madeinlab.feature.login.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.gentalha.code.madeinlab.core.rules.MainDispatcherRule
import io.gentalha.code.madeinlab.feature.login.domain.model.ValidationResult
import io.gentalha.code.madeinlab.feature.login.domain.usecase.*
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginEvent
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @MockK
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        every { validateEmailUseCase(any()) } returns ValidationResult(isSuccess = true)
        every { validatePasswordUseCase(any()) } returns ValidationResult(isSuccess = true)

        viewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )
    }

    @Test
    fun `initial state is correct`() {
        val initialState = viewModel.uiState.value

        assertThat(initialState.email).isEmpty()
        assertThat(initialState.password).isEmpty()
        assertThat(initialState.emailError).isNull()
        assertThat(initialState.passwordError).isNull()
        assertThat(initialState.isLoading).isFalse()
        assertThat(initialState.isFormValid).isFalse()
    }

    @Test
    fun `onLoginClicked with invalid email updates state and does not call login`() = runTest {
        val email = "invalid"
        val password = "password123"
        val errorMessage = "Email inválido"

        every { validateEmailUseCase(email) } returns ValidationResult(isSuccess = false, errorMessage = errorMessage)

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)
        viewModel.onLoginClicked()

        val finalState = viewModel.uiState.value
        assertThat(finalState.emailError).isEqualTo(errorMessage)
        assertThat(finalState.passwordError).isNull()

        coVerify(exactly = 0) { loginUseCase(any(), any()) }
    }

    @Test
    fun `onLoginClicked with valid data and success response sends success event`() = runTest {
        val email = "user@test.com"
        val password = "password123"

        coEvery { loginUseCase(email, password) } returns Result.success(Unit)

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.eventChannel.test {
            viewModel.onLoginClicked()

            assertThat(awaitItem()).isEqualTo(LoginEvent.LoginSuccess)
            cancelAndIgnoreRemainingEvents()
        }

        val finalState = viewModel.uiState.value
        assertThat(finalState.isLoading).isFalse()

        coVerify(exactly = 1) { loginUseCase(email, password) }
    }

    @Test
    fun `onLoginClicked with valid data and failure response sends failure event`() = runTest {
        val email = "user@test.com"
        val password = "password123"
        val errorMessage = "Usuário ou senha inválidos"

        coEvery { loginUseCase(email, password) } returns Result.failure(Exception(errorMessage))

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)
        viewModel.onLoginClicked()

        viewModel.eventChannel.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginEvent.LoginFailure::class.java)
            assertThat((event as LoginEvent.LoginFailure).message).isEqualTo(errorMessage)
        }

        coVerify(exactly = 1) { loginUseCase(email, password) }
    }
}