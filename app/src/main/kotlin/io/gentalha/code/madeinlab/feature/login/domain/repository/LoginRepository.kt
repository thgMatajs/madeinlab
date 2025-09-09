package io.gentalha.code.madeinlab.feature.login.domain.repository

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}