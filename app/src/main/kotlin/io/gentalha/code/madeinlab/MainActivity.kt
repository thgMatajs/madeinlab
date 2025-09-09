package io.gentalha.code.madeinlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.gentalha.code.madeinlab.feature.login.presentation.ui.screen.LoginScreen
import io.gentalha.code.madeinlab.ui.theme.MadeInLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MadeInLabTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.ime)
                    ) { innerPadding ->
                    LoginScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}