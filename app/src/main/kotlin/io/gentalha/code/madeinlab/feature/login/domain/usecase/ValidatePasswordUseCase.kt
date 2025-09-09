package io.gentalha.code.madeinlab.feature.login.domain.usecase

import io.gentalha.code.madeinlab.core.validation.Rules
import io.gentalha.code.madeinlab.feature.login.domain.model.ValidationResult
import org.koin.core.annotation.Factory

@Factory
class ValidatePasswordUseCase {

    private val passwordRules = listOf(
        Rules.NotEmptyRule("A senha não pode ser vazia."),
        Rules.MinLengthRule(6, "A senha deve ter no mínimo 6 caracteres.")
    )

    operator fun invoke(password: String): ValidationResult {
        val failingRule = passwordRules.firstOrNull { !it.validate(password) }
        return if (failingRule == null) {
            ValidationResult(isSuccess = true)
        } else {
            ValidationResult(isSuccess = false, errorMessage = failingRule.errorMessage)
        }
    }
}