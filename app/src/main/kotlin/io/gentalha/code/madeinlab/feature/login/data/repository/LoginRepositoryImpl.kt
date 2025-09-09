package io.gentalha.code.madeinlab.feature.login.data.repository

import io.gentalha.code.madeinlab.feature.login.domain.repository.LoginRepository
import io.gentalha.code.madeinlab.feature.login.remote.model.LoginRequest
import io.gentalha.code.madeinlab.feature.login.remote.service.LoginService
import org.koin.core.annotation.Single

@Single(binds = [LoginRepository::class])
class LoginRepositoryImpl(
    private val service: LoginService
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<Unit> {
        return service.login(LoginRequest(email, password))
            .onSuccess { token ->
                println("saved token..., token: $token")
            }
            .onFailure { exception ->
                println("Login failed: ${exception.message}")
            }
            .map {}
    }
}