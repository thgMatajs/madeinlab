package io.gentalha.code.madeinlab.core.validation

/**
 * Interface base para todas as regras de validação.
 */
interface ValidationRule {
    /**
     * A mensagem de erro a ser exibida se a validação falhar.
     */
    val errorMessage: String

    /**
     * Valida o input.
     * @param input O texto a ser validado.
     * @return `true` se o input for válido, `false` caso contrário.
     */
    fun validate(input: String): Boolean
}