package io.gentalha.code.madeinlab.feature.login.presentation.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.gentalha.code.madeinlab.R
import io.gentalha.code.madeinlab.core.extesions.shortToast
import io.gentalha.code.madeinlab.ds.buttom.PrimaryButton
import io.gentalha.code.madeinlab.ds.buttom.SecondaryButton
import io.gentalha.code.madeinlab.ds.textfield.AppTextField
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.*
import io.gentalha.code.madeinlab.feature.login.presentation.viewmodel.LoginViewModel
import io.gentalha.code.madeinlab.ui.theme.MadeInLabTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    var hasEmailBeenFocused by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    val successMessage = stringResource(R.string.login_success)
    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                is LoginEvent.LoginSuccess -> shortToast(successMessage, context)
                is LoginEvent.LoginFailure -> shortToast(event.message, context)
            }
        }
    }

    val actions = remember(viewModel) {
        LoginActions(
            onEmailChanged = viewModel::onEmailChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onLoginClicked = {
                focusManager.clearFocus()
                viewModel.onLoginClicked()
            },
            onEmailFocusChanged = { isFocused ->
                if (isFocused) hasEmailBeenFocused = true
                if (!isFocused && hasEmailBeenFocused) viewModel.onEmailFocusChanged()
            },
            onImeActionNext = { focusManager.moveFocus(FocusDirection.Down) },
            onImeActionDone = {
                focusManager.clearFocus()
                viewModel.onLoginClicked()
            },
            onForgotPasswordClicked = { shortToast("Navega para esqueceu minha senha.", context) },
            onGoogleLoginClicked = { shortToast("Faz login com o google.", context) },
            onCreateAccountClicked = { shortToast("Navega para Criar conta.", context) }
        )
    }

    LoginScreenContent(
        modifier = modifier,
        uiState = uiState,
        emailFocusRequester = emailFocusRequester,
        actions = actions
    )
}

@Composable
fun AppLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = stringResource(R.string.app_logo_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    emailFocusRequester: FocusRequester,
    actions: LoginActions
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 58.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.login_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
            AppTextField(
                value = uiState.email,
                onValueChange = actions.onEmailChanged,
                label = stringResource(R.string.login_email),
                leadingIcon = Icons.Default.Mail,
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { actions.onImeActionNext() }),
                modifier = Modifier
                    .focusRequester(emailFocusRequester)
                    .testTag("EmailField")
                    .onFocusChanged { focusState -> actions.onEmailFocusChanged(focusState.isFocused) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                modifier = Modifier.testTag("PasswordField"),
                value = uiState.password,
                onValueChange = actions.onPasswordChanged,
                label = stringResource(R.string.login_password),
                leadingIcon = Icons.Default.Lock,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { actions.onImeActionDone() })
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = actions.onForgotPasswordClicked,
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login_forgot_password),
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                modifier = Modifier.testTag("LoginButton"),
                text = stringResource(R.string.login_button),
                isLoading = uiState.isLoading,
                onClick = actions.onLoginClicked,
                enabled = uiState.isFormValid && !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                text = stringResource(R.string.login_google_button),
                icon = Icons.Default.AccountCircle,
                onClick = actions.onGoogleLoginClicked
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.login_no_account),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = actions.onCreateAccountClicked,
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.login_signup),
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun LoginScreenPreviewLight() {
    MadeInLabTheme(darkTheme = false) {
        LoginScreen()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun LoginScreenPreviewDark() {
    MadeInLabTheme(darkTheme = true) {
        LoginScreen()
    }
}