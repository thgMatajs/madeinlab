package io.gentalha.code.madeinlab.core.validation

import androidx.core.util.PatternsCompat

/**
 * Conjunto de regras de validação para campos de texto.
 *
 * Cada regra implementa a interface [ValidationRule] e pode ser utilizada para validar entradas de usuário.
 */
object Rules {

    /**
     * Regra que valida se o campo não está vazio.
     * @property errorMessage Mensagem exibida quando a validação falha.
     */
    class NotEmptyRule(
        override val errorMessage: String = "Este campo não pode ser vazio."
    ) : ValidationRule {
        override fun validate(input: String): Boolean = input.isNotBlank()
    }

    /**
     * Regra que valida se o campo possui um tamanho mínimo.
     * @property minLength Tamanho mínimo permitido.
     * @property errorMessage Mensagem exibida quando a validação falha.
     */
    class MinLengthRule(
        private val minLength: Int,
        override val errorMessage: String = "Deve ter no mínimo $minLength caracteres."
    ) : ValidationRule {
        override fun validate(input: String): Boolean = input.length >= minLength
    }

    /**
     * Regra que valida se o campo possui formato de e-mail válido.
     * @property errorMessage Mensagem exibida quando a validação falha.
     */
    class EmailRule(
        override val errorMessage: String = "Formato de e-mail inválido."
    ) : ValidationRule {
        override fun validate(input: String): Boolean = PatternsCompat.EMAIL_ADDRESS.matcher(input).matches()
    }
}