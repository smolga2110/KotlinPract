package ru.mazenin.kotlin

import android.app.Application
import androidx.lifecycle.*
import java.io.File

data class ImageState(
    val busy: Boolean = false,
    val message: String = "Готов к загрузке",
    val latest: File? = null,
    val history: List<File> = emptyList(),
)

class ImageViewModel(app: Application) : AndroidViewModel(app) {
    private val folder = File(app.filesDir, "images")
    private val downloader = ImageDownloader(folder)
    private val mutable = MutableLiveData(ImageState(history = files()))
    val state: LiveData<ImageState> = mutable

    private fun files() =
        folder
            .listFiles()
            ?.filter { it.isFile && it.extension in listOf("png", "jpg") }
            ?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun load(url: String) {
        if (mutable.value?.busy == true) return
        try {
            ImagePolicy.validate(url)
        } catch (e: Exception) {
            mutable.value = mutable.value!!.copy(message = e.message ?: "Некорректная ссылка")
            return
        }
        mutable.value =
            mutable.value!!.copy(busy = true, message = "Network: загрузка · Disk: ожидание")
        downloader.download(url) { result ->
            result.fold(
                onSuccess = {
                    mutable.postValue(
                        ImageState(
                            message =
                                "Сохранено: ${it.file.name}\n${it.bytes} байт · ${it.networkThread} → ${it.diskThread}",
                            latest = it.file,
                            history = files(),
                        )
                    )
                },
                onFailure = {
                    mutable.postValue(
                        ImageState(
                            message = "Ошибка загрузки: ${it.cause?.message ?: it.message}",
                            history = files(),
                        )
                    )
                },
            )
        }
    }

    override fun onCleared() {
        downloader.close()
    }
}
