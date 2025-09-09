package io.gentalha.code.madeinlab.feature.login.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    @Before
    fun setUp() {
        validatePasswordUseCase = ValidatePasswordUseCase()
    }

    @Test
    fun `invoke with valid password should return success`() {
        val result = validatePasswordUseCase("123456")
        assertThat(result.isSuccess).isTrue()
        assertThat(result.errorMessage).isNull()
    }

    @Test
    fun `invoke with empty password should return failure with not empty message`() {
        val result = validatePasswordUseCase("")
        assertThat(result.isSuccess).isFalse()
        assertThat(result.errorMessage).isEqualTo("A senha não pode ser vazia.")
    }

    @Test
    fun `invoke with short password should return failure with min length message`() {
        val result = validatePasswordUseCase("12345")
        assertThat(result.isSuccess).isFalse()
        assertThat(result.errorMessage).isEqualTo("A senha deve ter no mínimo 6 caracteres.")
    }
}