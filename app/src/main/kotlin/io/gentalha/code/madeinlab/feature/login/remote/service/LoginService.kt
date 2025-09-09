package io.gentalha.code.madeinlab.feature.login.remote.service

import io.gentalha.code.madeinlab.feature.login.remote.model.LoginRequest
import kotlinx.coroutines.delay
import org.koin.core.annotation.Single

private const val SIMULATION_TIME_RESPONSE = 2000L

@Single
class LoginService {
    suspend fun login(request: LoginRequest): Result<String> {
        delay(SIMULATION_TIME_RESPONSE)
        return when {
            request.email == "error@error.com" -> return Result.failure(Exception("Email não encontrado..."))
            request.password == "123456" -> return Result.failure(Exception("Senha incorreta..."))
            else -> {
                Result.success("token_example_123456")
            }
        }
    }
}