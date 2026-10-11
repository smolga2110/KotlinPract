package ru.mazenin.kotlin

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun DownloadScreen(model: ImageViewModel, modifier: Modifier = Modifier) {
    val state by model.state.observeAsState(ImageState())
    var url by rememberSaveable { mutableStateOf("https://httpbin.org/image/png") }
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("Загрузчик изображений", style = MaterialTheme.typography.titleLarge)
        Text("Два потока: Network → Disk", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            url,
            { url = it },
            Modifier.fillMaxWidth(),
            label = { Text("Ссылка на PNG или JPEG") },
            singleLine = true,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { model.load(url) }, enabled = !state.busy, modifier = Modifier.fillMaxWidth()) {
                Text("Загрузить изображение")
            }
        }
        Text(state.message, style = MaterialTheme.typography.bodySmall)
        Box(Modifier.fillMaxWidth().height(213.dp), contentAlignment = Alignment.Center) {
            val bitmap =
                remember(state.latest) {
                    state.latest?.let { BitmapFactory.decodeFile(it.absolutePath)?.asImageBitmap() }
                }
            if (bitmap != null)
                Image(
                    bitmap,
                    "Загруженное изображение",
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            else Text("Предпросмотр изображения", color = MaterialTheme.colorScheme.secondary)
        }
        Text("История загрузок", style = MaterialTheme.typography.titleMedium)
        HistoryList(state.history, Modifier.weight(1f))
    }
}

@Composable
fun HistoryList(files: List<File>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        if (files.isEmpty()) item { Text("Пока нет сохранённых изображений") }
        items(files, key = { it.name }) { file ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(file.name, style = MaterialTheme.typography.bodyMedium)
                    Text("${file.length()} байт", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun HistoryScreen(
    model: ImageViewModel,
    modifier: Modifier = Modifier,
    extra: @Composable () -> Unit = {},
) {
    val state by model.state.observeAsState(ImageState())
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text("Сохранено: ${state.history.size}", style = MaterialTheme.typography.titleLarge)
        extra()
        HistoryList(state.history, Modifier.weight(1f))
    }
}

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Моя фотоколлекция", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Введите ссылку, загрузите изображение и найдите его в истории. Файлы сохраняются во внутренней памяти приложения."
        )
        Text("Мазенин Артем Андреевич\nИКБО-63-23", color = MaterialTheme.colorScheme.primary)
    }
}
