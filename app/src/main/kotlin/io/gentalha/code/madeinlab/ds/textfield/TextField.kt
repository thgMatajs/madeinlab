package io.gentalha.code.madeinlab.ds.textfield

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gentalha.code.madeinlab.ui.theme.MadeInLabTheme

/**
 * Componente de campo de texto unificado e reutilizável para a aplicação.
 * Ele se adapta automaticamente para campos de senha com base no KeyboardType.
 *
 * @param value O valor atual do campo.
 * @param onValueChange Callback acionado quando o valor muda.
 * @param label O texto do rótulo que flutua acima do campo.
 * @param modifier Modificador para customização.
 * @param leadingIcon Ícone opcional à esquerda.
 * @param isError Flag que indica se o campo está em estado de erro.
 * @param errorMessage Mensagem de erro a ser exibida abaixo do campo.
 * @param keyboardOptions Opções de teclado (tipo, imeAction).
 * @param keyboardActions Ações do teclado (onDone, onNext).
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val isPasswordType = keyboardOptions.keyboardType == KeyboardType.Password
    var isPasswordVisible by remember { mutableStateOf(false) }

    val visualTransformation = when {
        isPasswordType && !isPasswordVisible -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val trailingIcon = @Composable {
        if (isPasswordType) {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (isPasswordVisible) "Esconder senha" else "Mostrar senha"
                )
            }
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(imageVector = leadingIcon, contentDescription = null)
                }
            },
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                errorTextColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorLabelColor = MaterialTheme.colorScheme.error
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
                    .heightIn(min = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "AppTextField Preview")
@Composable
private fun AppTextFieldPreview() {
    MadeInLabTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("12345") }

            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(16.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                label = "Senha",
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(16.dp))

            AppTextField(
                value = "123",
                onValueChange = { },
                label = "Senha com Erro",
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = true,
                errorMessage = "A senha precisa ter no mínimo 6 caracteres."
            )
        }
    }
}