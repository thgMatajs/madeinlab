package io.gentalha.code.madeinlab.feature.login.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.google.common.truth.Truth.assertThat
import io.gentalha.code.madeinlab.feature.login.presentation.ui.screen.LoginScreenContent
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginActions
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginUiState
import io.gentalha.code.madeinlab.ui.theme.MadeInLabTheme
import org.junit.Rule
import org.junit.Test

@SuppressLint("RememberInComposition")
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialState_displaysAllCoreComponents() {
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions()
                )
            }
        }

        composeTestRule.onNodeWithTag("EmailField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("PasswordField").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoginButton").assertIsDisplayed()
    }

    @SuppressLint("RememberInComposition")
    @Test
    fun stateWithError_displaysEmailErrorMessage() {
        val errorMessage = "E-mail inválido"

        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(emailError = errorMessage),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions()
                )
            }
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun typingInEmailField_callsOnEmailChangedAction() {
        var capturedEmail = ""
        val typedText = "user@test.com"

        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions(
                        onEmailChanged = { capturedEmail = it }
                    )
                )
            }
        }

        composeTestRule.onNodeWithTag("EmailField").performTextInput(typedText)

        assertThat(capturedEmail).isEqualTo(typedText)
    }

    // Testa se o botão de login está desabilitado quando o formulário é inválido
    @Test
    fun invalidFormState_loginButtonIsDisabled() {
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(isFormValid = false),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions()
                )
            }
        }

        composeTestRule.onNodeWithTag("LoginButton").assertIsNotEnabled()
    }

    // Testa se o botão de login está habilitado quando o formulário é válido
    @Test
    fun validFormState_loginButtonIsEnabled() {
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(isFormValid = true),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions()
                )
            }
        }

        composeTestRule.onNodeWithTag("LoginButton").assertIsEnabled()
    }

    // Testa se o indicador de loading aparece quando isLoading = true
    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(isLoading = true),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions()
                )
            }
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoginButton").assertIsNotEnabled() // Botão deve desabilitar no loading
    }

    // Testa se a ação de clique no botão de login é chamada
    @Test
    fun loginButtonClick_callsOnLoginClickedAction() {
        var wasClicked = false
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(isFormValid = true), // Precisa estar válido para ser clicável
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions(onLoginClicked = { wasClicked = true })
                )
            }
        }

        composeTestRule.onNodeWithTag("LoginButton").performClick()

        assertThat(wasClicked).isTrue()
    }

    // Testa o clique no botão "Esqueceu a senha?"
    @Test
    fun forgotPasswordClick_callsOnForgotPasswordClickedAction() {
        var wasClicked = false
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions(onForgotPasswordClicked = { wasClicked = true })
                )
            }
        }

        composeTestRule.onNodeWithTag("ForgotPasswordButton").performClick()

        assertThat(wasClicked).isTrue()
    }

    // Testa a ação "Next" do teclado no campo de e-mail
    @Test
    fun emailImeActionNext_callsOnImeActionNext() {
        var wasCalled = false
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions(onImeActionNext = { wasCalled = true })
                )
            }
        }

        composeTestRule.onNodeWithTag("EmailField").performImeAction()

        assertThat(wasCalled).isTrue()
    }

    // Testa a ação "Done" do teclado no campo de senha
    @Test
    fun passwordImeActionDone_callsOnImeActionDone() {
        var wasCalled = false
        composeTestRule.setContent {
            MadeInLabTheme {
                LoginScreenContent(
                    uiState = LoginUiState(),
                    emailFocusRequester = FocusRequester(),
                    actions = createFakeActions(onImeActionDone = { wasCalled = true })
                )
            }
        }

        composeTestRule.onNodeWithTag("PasswordField").performImeAction()

        assertThat(wasCalled).isTrue()
    }

    private fun createFakeActions(
        onEmailChanged: (String) -> Unit = {},
        onPasswordChanged: (String) -> Unit = {},
        onLoginClicked: () -> Unit = {},
        onEmailFocusChanged: (isFocused: Boolean) -> Unit = {},
        onImeActionNext: () -> Unit = {},
        onImeActionDone: () -> Unit = {},
        onForgotPasswordClicked: () -> Unit = {},
        onGoogleLoginClicked: () -> Unit = {},
        onCreateAccountClicked: () -> Unit = {},
    ) = LoginActions(
        onEmailChanged = onEmailChanged,
        onPasswordChanged = onPasswordChanged,
        onLoginClicked = onLoginClicked,
        onEmailFocusChanged = onEmailFocusChanged,
        onImeActionNext = onImeActionNext,
        onImeActionDone = onImeActionDone,
        onForgotPasswordClicked = onForgotPasswordClicked,
        onGoogleLoginClicked = onGoogleLoginClicked,
        onCreateAccountClicked = onCreateAccountClicked
    )
}