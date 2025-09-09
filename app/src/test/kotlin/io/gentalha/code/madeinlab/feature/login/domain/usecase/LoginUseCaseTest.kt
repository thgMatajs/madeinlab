package io.gentalha.code.madeinlab.feature.login.domain.usecase

import com.google.common.truth.Truth.assertThat
import io.gentalha.code.madeinlab.feature.login.domain.model.ValidationResult
import io.gentalha.code.madeinlab.feature.login.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.*

class LoginUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    private lateinit var repository: LoginRepository
    @MockK
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    @MockK
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(
            repository = repository,
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )
    }

    @Test
    fun `invoke with valid credentials and repository success should return success`() = runTest {
        val email = "user@test.com"
        val password = "password123"
        coEvery { validateEmailUseCase(email) } returns ValidationResult(isSuccess = true)
        coEvery { validatePasswordUseCase(password) } returns ValidationResult(isSuccess = true)
        coEvery { repository.login(email, password) } returns Result.success(Unit)

        val result = loginUseCase(email, password)

        assertThat(result.isSuccess).isTrue()
        coVerify(exactly = 1) { repository.login(email, password) }
    }

    @Test
    fun `invoke with invalid email should return failure and not call repository`() = runTest {
        val invalidEmail = "invalid-email"
        val password = "password123"
        val errorMessage = "Email inválido"
        coEvery { validateEmailUseCase(invalidEmail) } returns ValidationResult(isSuccess = false, errorMessage = errorMessage)

        val result = loginUseCase(invalidEmail, password)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(errorMessage)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun `invoke with invalid password should return failure and not call repository`() = runTest {
        // Arrange
        val email = "user@test.com"
        val shortPassword = "123"
        val errorMessage = "Senha curta"

        coEvery { validateEmailUseCase(email) } returns ValidationResult(isSuccess = true)
        coEvery { validatePasswordUseCase(shortPassword) } returns ValidationResult(isSuccess = false, errorMessage = errorMessage)

        val result = loginUseCase(email, shortPassword)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(errorMessage)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun `invoke with valid credentials but repository failure should return failure`() = runTest {
        // Arrange
        val email = "user@test.com"
        val password = "password123"
        val errorMessage = "Usuário ou senha inválidos"

        coEvery { validateEmailUseCase(email) } returns ValidationResult(isSuccess = true)
        coEvery { validatePasswordUseCase(password) } returns ValidationResult(isSuccess = true)
        coEvery { repository.login(email, password) } returns Result.failure(Exception(errorMessage))

        val result = loginUseCase(email, password)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(errorMessage)
        coVerify(exactly = 1) { repository.login(email, password) }
    }
}