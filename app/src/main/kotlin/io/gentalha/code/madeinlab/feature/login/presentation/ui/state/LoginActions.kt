package io.gentalha.code.madeinlab.feature.login.presentation.ui.state

data class LoginActions(
    val onEmailChanged: (String) -> Unit,
    val onPasswordChanged: (String) -> Unit,
    val onLoginClicked: () -> Unit,
    val onEmailFocusChanged: (isFocused: Boolean) -> Unit,
    val onImeActionNext: () -> Unit,
    val onImeActionDone: () -> Unit,
    val onForgotPasswordClicked: () -> Unit,
    val onGoogleLoginClicked: () -> Unit,
    val onCreateAccountClicked: () -> Unit,
)