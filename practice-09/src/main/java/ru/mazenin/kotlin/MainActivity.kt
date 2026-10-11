package ru.mazenin.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentTheme {
                Surface(Modifier.fillMaxSize()) {
                    Column(
                        Modifier.safeDrawingPadding().padding(28.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            "ПРАКТИЧЕСКАЯ РАБОТА 9",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            "Мазенин\nАртем Андреевич",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Spacer(Modifier.height(18.dp))
                        Text(
                            "ИКБО-63-23",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.height(32.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(20.dp))
                        Text(
                            "Проектирование и разработка мобильных приложений на языке Котлин",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}
