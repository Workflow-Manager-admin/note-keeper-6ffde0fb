package com.example.frontendnotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.lightColors
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendnotesapp.ui.NotesApp
import com.example.frontendnotesapp.ui.NotesViewModel
import com.example.frontendnotesapp.ui.NotesViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppRoot(this)
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun NotesAppRoot(context: android.content.Context) {
    MaterialTheme(
        colors = lightColors(
            primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
            primaryVariant = androidx.compose.ui.graphics.Color(0xFF3700B3),
            secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6),
            secondaryVariant = androidx.compose.ui.graphics.Color(0xFF018786),
            surface = androidx.compose.ui.graphics.Color.White,
            background = androidx.compose.ui.graphics.Color.White,
            error = androidx.compose.ui.graphics.Color(0xFFB00020),
            onPrimary = androidx.compose.ui.graphics.Color.White,
            onSecondary = androidx.compose.ui.graphics.Color(0xFF333333),
            onSurface = androidx.compose.ui.graphics.Color(0xFF000000),
            onBackground = androidx.compose.ui.graphics.Color(0xFF222222),
            onError = androidx.compose.ui.graphics.Color.White
        )
    ) {
        val viewModel: NotesViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
            factory = com.example.frontendnotesapp.ui.NotesViewModelFactory(context)
        )
        Surface {
            com.example.frontendnotesapp.ui.NotesApp(viewModel)
        }
    }
}
