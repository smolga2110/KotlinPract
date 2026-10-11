package ru.mazenin.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val model = ViewModelProvider(this)[ImageViewModel::class.java]
        setContent {
            StudentTheme {
                Surface(Modifier.fillMaxSize()) {
                    DownloadScreen(model, Modifier.safeDrawingPadding())
                }
            }
        }
    }
}
