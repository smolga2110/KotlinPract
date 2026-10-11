package ru.mazenin.kotlin

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val model = ViewModelProvider(this)[ImageViewModel::class.java]
        val page = column()
        page.label("Загрузчик изображений", 24f)
        page.label("Два потока: Network → Disk", 15f)
        val url =
            page.input("Ссылка на PNG или JPEG", "https://httpbin.org/image/png").apply {
                id = R.id.url_input
                inputType = 17
            }
        val download =
            page
                .button("Загрузить изображение") { model.load(url.text.toString()) }
                .apply { id = R.id.download_button }
        val status = page.label("", 15f).apply { id = R.id.status_text }
        val preview =
            ImageView(this).apply {
                id = R.id.preview_image
                contentDescription = "Загруженное изображение"
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        page.addView(preview, LinearLayout.LayoutParams(-1, 320))
        page.label("История загрузок", 20f).id = R.id.history_title
        val scroll = ScrollView(this)
        val history = column()
        scroll.addView(history)
        page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        model.state.observe(this) { s ->
            download.isEnabled = !s.busy
            status.text = s.message
            s.latest?.let { preview.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath)) }
            history.removeAllViews()
            if (s.history.isEmpty()) history.label("Пока нет сохранённых изображений", 15f)
            s.history.forEach { history.label("${it.name} · ${it.length()} байт", 14f) }
        }
        showRoot(page)
    }
}
