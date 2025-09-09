package io.gentalha.code.madeinlab.feature.login.domain.usecase

import io.gentalha.code.madeinlab.feature.login.domain.repository.LoginRepository
import org.koin.core.annotation.Factory

@Factory
class LoginUseCase(
    private val repository: LoginRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val emailResult = validateEmailUseCase(email)
        if (!emailResult.isSuccess) {
            return Result.failure(Exception(emailResult.errorMessage))
        }

        val passwordResult = validatePasswordUseCase(password)
        if (!passwordResult.isSuccess) {
            return Result.failure(Exception(passwordResult.errorMessage))
        }
        return repository.login(email, password)
    }
}