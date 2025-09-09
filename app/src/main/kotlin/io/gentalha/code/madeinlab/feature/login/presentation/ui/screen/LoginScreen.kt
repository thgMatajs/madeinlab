package io.gentalha.code.madeinlab.feature.login.presentation.ui.screen

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gentalha.code.madeinlab.R
import io.gentalha.code.madeinlab.ds.buttom.PrimaryButton
import io.gentalha.code.madeinlab.ds.buttom.SecondaryButton
import io.gentalha.code.madeinlab.ds.textfield.AppTextField
import io.gentalha.code.madeinlab.feature.login.presentation.ui.state.LoginEvent
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
                is LoginEvent.LoginSuccess -> {
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                }
                is LoginEvent.LoginFailure -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp),
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
                onValueChange = viewModel::onEmailChanged,
                label = stringResource(R.string.login_email),
                leadingIcon = Icons.Default.Mail,
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier
                    .focusRequester(emailFocusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            hasEmailBeenFocused = true
                        }
                        if (!focusState.isFocused && hasEmailBeenFocused) {
                            viewModel.onEmailFocusChanged()
                        }
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = stringResource(R.string.login_password),
                leadingIcon = Icons.Default.Lock,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.onLoginClicked()
                    }
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { /* TODO: Chamar viewModel.onForgotPasswordClicked() */ },
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
                text = stringResource(R.string.login_button),
                isLoading = uiState.isLoading,
                onClick = {
                    viewModel.onLoginClicked()
                    focusManager.clearFocus()
                },
                enabled = uiState.isFormValid && !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                text = stringResource(R.string.login_google_button),
                icon = Icons.Default.AccountCircle,
                onClick = { /* TODO: Chamar viewModel.onGoogleSignInClicked() */ }
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
                    onClick = { /* TODO: Chamar viewModel.onSignUpClicked() */ },
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

@Composable
fun AppLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_compass),
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

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun LoginScreenPreviewLight() {
    MadeInLabTheme(darkTheme = false) {
        LoginScreen(Modifier)
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun LoginScreenPreviewDark() {
    MadeInLabTheme(darkTheme = true) {
        LoginScreen(Modifier)
    }
}
