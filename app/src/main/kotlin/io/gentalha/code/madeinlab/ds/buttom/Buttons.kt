package io.gentalha.code.madeinlab.ds.buttom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gentalha.code.madeinlab.ui.theme.MadeInLabTheme

/**
 * Botão primário
 *
 * @param text O texto a ser exibido no botão.
 * @param onClick A ação a ser executada quando o botão é clicado.
 * @param modifier O modificador a ser aplicado ao botão.
 * @param isLoading Flag para controlar a exibição do estado de loading.
 * @param enabled Flag para controlar se o botão está habilitado.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        ),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 3.dp
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

/**
 * Botão secundário.
 *
 * @param text O texto a ser exibido no botão.
 * @param onClick A ação a ser executada quando o botão é clicado.
 * @param modifier O modificador a ser aplicado ao botão.
 * @param isLoading Flag para controlar a exibição do estado de loading.
 * @param enabled Flag para controlar se o botão está habilitado.
 * @param icon Um ícone opcional a ser exibido à esquerda do texto.
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onSurface,
                strokeWidth = 3.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Button States")
@Composable
private fun PrimaryButtonPreview() {
    MadeInLabTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(text = "Estado Padrão", onClick = { })
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Estado de Loading", onClick = { }, isLoading = true)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Estado Desabilitado", onClick = { }, enabled = false)
        }
    }
}

@Preview(showBackground = true, name = "Secondary Button States")
@Composable
private fun SecondaryButtonPreview() {
    MadeInLabTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SecondaryButton(text = "Estado Padrão", onClick = { })
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(
                text = "Com Ícone",
                icon = Icons.Default.AccountCircle,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Estado de Loading", onClick = { }, isLoading = true)
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Estado Desabilitado", onClick = { }, enabled = false)
        }
    }
}