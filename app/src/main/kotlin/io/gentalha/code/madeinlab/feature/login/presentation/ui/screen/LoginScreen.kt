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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gentalha.code.madeinlab.core.validation.Rules
import io.gentalha.code.madeinlab.core.validation.ValidatedState
import io.gentalha.code.madeinlab.ds.buttom.PrimaryButton
import io.gentalha.code.madeinlab.ds.buttom.SecondaryButton
import io.gentalha.code.madeinlab.ds.textfield.AppTextField
import io.gentalha.code.madeinlab.ui.theme.MadeInLabTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val emailState = remember {
        ValidatedState(rules = listOf(
            Rules.NotEmptyRule("O e-mail não pode ser vazio."),
            Rules.EmailRule("O formato do e-mail é inválido.")
        ))
    }
    val passwordState = remember {
        ValidatedState(rules = listOf(
            Rules.NotEmptyRule("A senha não pode ser vazia."),
            Rules.MinLengthRule(6, "A senha deve ter no mínimo 6 caracteres.")
        ))
    }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }

    var hasEmailBeenFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    val isFormValid by remember {
        derivedStateOf {
            val isEmailValid = emailState.rules.all { it.validate(emailState.text) }
            val isPasswordValid = passwordState.rules.all { it.validate(passwordState.text) }
            isEmailValid && isPasswordValid
        }
    }

    val performLogin = {
        emailState.validate()
        passwordState.validate()
        if (isFormValid) {
            focusManager.clearFocus()
            scope.launch {
                isLoading = true
                delay(2000)
                isLoading = false
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
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
                .padding(32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Bem-vindo de volta!",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            AppTextField(
                value = emailState.text,
                onValueChange = { emailState.onValueChanged(it) },
                label = "Email",
                leadingIcon = Icons.Default.Mail,
                isError = emailState.isError,
                errorMessage = emailState.errorMessage,
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
                            emailState.validate()
                        }
                    }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = passwordState.text,
                onValueChange = { passwordState.onValueChanged(it) },
                label = "Senha",
                leadingIcon = Icons.Default.Lock,
                isError = passwordState.isError,
                errorMessage = passwordState.errorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { performLogin() }
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = "Esqueceu a senha?",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Entrar",
                isLoading = isLoading,
                onClick = { performLogin() },
                enabled = isFormValid && !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                text = "Entrar com Google",
                icon = Icons.Default.AccountCircle,
                onClick = { }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Não tem uma conta?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = "Crie uma aqui.",
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
            text = "madeInLab",
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
