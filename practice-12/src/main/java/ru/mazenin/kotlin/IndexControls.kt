package ru.mazenin.kotlin

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.work.*

@Composable
fun IndexControls() {
    val manager = WorkManager.getInstance(LocalContext.current)
    val records by
        remember(manager) { manager.getWorkInfosForUniqueWorkLiveData("image-index") }
            .observeAsState(emptyList())
    val work = records.firstOrNull()
    Button(
        onClick = {
            manager.enqueueUniqueWork(
                "image-index",
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<ImageIndexWorker>().build(),
            )
        }
    ) {
        Text("Создать индекс файлов")
    }
    Text(
        when (work?.state) {
            WorkInfo.State.SUCCEEDED ->
                "Индекс создан: ${work.outputData.getInt("count",0)} шт.\nimage-index.json · SHA-256"
            WorkInfo.State.FAILED -> "Ошибка: ${work.outputData.getString("error")}"
            WorkInfo.State.RUNNING -> "Создание индекса…"
            WorkInfo.State.ENQUEUED -> "Задача ожидает запуска"
            else -> "Индекс содержит размеры и контрольные суммы изображений"
        },
        style = MaterialTheme.typography.bodySmall,
    )
}

