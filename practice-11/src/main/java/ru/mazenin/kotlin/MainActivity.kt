package ru.mazenin.kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val model = ViewModelProvider(this)[ImageViewModel::class.java]
        setContent { StudentTheme { AppShell(model) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(model: ImageViewModel) {
    val pages = listOf("download" to "Загрузка", "history" to "История", "about" to "О приложении")
    var current by rememberSaveable { mutableStateOf("download") }
    fun select(route: String) {
        current = route
    }
    BackHandler(current != "download") { current = "download" }

    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawer,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Моя фотоколлекция",
                    Modifier.padding(24.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                pages.forEach { (route, title) ->
                    NavigationDrawerItem(
                        label = { Text(title) },
                        selected = current == route,
                        onClick = {
                            select(route)
                            scope.launch { drawer.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(pages.first { it.first == current }.second) },
                    navigationIcon = {
                        TextButton(onClick = { scope.launch { drawer.open() } }) { Text("Меню") }
                    },
                )
            },
            bottomBar = {
                BottomAppBar {
                    pages.forEach { (route, title) ->
                        TextButton(onClick = { select(route) }, modifier = Modifier.weight(1f)) {
                            Text(if (current == route) "• $title" else title)
                        }
                    }
                }
            },
        ) { padding ->
            when (current) {
                "download" -> DownloadScreen(model, Modifier.padding(padding))
                "history" -> HistoryScreen(model, Modifier.padding(padding))
                else -> AboutScreen(Modifier.padding(padding))
            }
        }
    }
}
