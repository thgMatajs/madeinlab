package io.gentalha.code.madeinlab.feature.login.domain.model

data class ValidationResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
)