package io.gentalha.code.madeinlab.core.validation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Gerencia o estado e a validação de um campo de texto.
 *
 * Esta classe é útil para formulários em Jetpack Compose, pois mantém o valor do campo,
 * a mensagem de erro e executa validações automaticamente ao alterar o texto.
 *
 * @property initialValue Valor inicial do campo de texto.
 * @property rules Lista de regras de validação aplicadas ao campo.
 * @property text Valor atual do campo de texto.
 * @property errorMessage Mensagem de erro atual, ou null se não houver erro.
 * @property isError Indica se o campo está em estado de erro.
 *
 * @constructor Cria um estado validado para um campo de texto.
 * @param initialValue Valor inicial do campo.
 * @param rules Lista de regras de validação a serem aplicadas.
 *
 * @see ValidationRule Para definir regras customizadas.
 */
class ValidatedState(
    val initialValue: String = "",
    val rules: List<ValidationRule> = emptyList()
) {
    var text by mutableStateOf(initialValue)
    var errorMessage by mutableStateOf<String?>(null)

    val isError: Boolean
        get() = errorMessage != null

    /**
     * Atualiza o valor do campo e executa a validação.
     * @param newText Novo valor do campo de texto.
     */
    fun onValueChanged(newText: String) {
        text = newText
        validate()
    }

    /**
     * Executa as regras de validação e atualiza a mensagem de erro.
     * Se houver erro, armazena a mensagem da primeira regra que falhar.
     */
    fun validate() {
        val firstError = rules.firstOrNull { !it.validate(text) }
        errorMessage = firstError?.errorMessage
    }
}