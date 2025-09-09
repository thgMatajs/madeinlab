package io.gentalha.code.madeinlab.feature.login.domain.usecase

import io.gentalha.code.madeinlab.core.validation.Rules
import io.gentalha.code.madeinlab.feature.login.domain.model.ValidationResult
import org.koin.core.annotation.Factory

@Factory
class ValidateEmailUseCase {

    private val emailRules = listOf(
        Rules.NotEmptyRule("O e-mail não pode ser vazio."),
        Rules.EmailRule("O formato do e-mail é inválido.")
    )

    operator fun invoke(email: String): ValidationResult {
        val failingRule = emailRules.firstOrNull { !it.validate(email) }
        return if (failingRule == null) {
            ValidationResult(isSuccess = true)
        } else {
            ValidationResult(isSuccess = false, errorMessage = failingRule.errorMessage)
        }
    }
}