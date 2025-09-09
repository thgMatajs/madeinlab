package io.gentalha.code.madeinlab.feature.login.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateEmailUseCaseTest {

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @Before
    fun setUp() {
        validateEmailUseCase = ValidateEmailUseCase()
    }

    @Test
    fun `invoke with valid email should return success`() {
        val result = validateEmailUseCase("contato@email.com")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.errorMessage).isNull()
    }

    @Test
    fun `invoke with empty email should return failure with not empty message`() {
        val result = validateEmailUseCase("")
        assertThat(result.isSuccess).isFalse()
        assertThat(result.errorMessage).isEqualTo("O e-mail não pode ser vazio.")
    }

    @Test
    fun `invoke with invalid format email should return failure with invalid format message`() {
        val result = validateEmailUseCase("contato-email.com")
        assertThat(result.isSuccess).isFalse()
        assertThat(result.errorMessage).isEqualTo("O formato do e-mail é inválido.")
    }
}