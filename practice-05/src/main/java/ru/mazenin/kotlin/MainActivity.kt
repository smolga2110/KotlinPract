package ru.mazenin.kotlin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*

class MainActivity : AppCompatActivity() {

    private lateinit var model: CatalogViewModel
    private var local = false

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        model =
            ViewModelProvider(
                this,
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(type: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return CatalogViewModel(
                            CatalogRepository(createApi(), CatalogDb(applicationContext))
                        )
                            as T
                    }
                },
            )[CatalogViewModel::class.java]
        local = state?.getBoolean("local") ?: false
        val page = column()
        page.label("Каталог товаров", 26f)
        page.label("Retrofit · SQLite · LiveData", 14f)
        val heading = page.label(if (local) "Локальная база" else "Внешний API", 22f)
        val tabs = LinearLayout(this)
        page.addView(tabs)
        tabs
            .button("API") {
                local = false
                heading.text = "Внешний API"
                model.load(true)
            }
            .layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
        tabs
            .button("База данных") {
                local = true
                heading.text = "Локальная база"
                model.load(false)
            }
            .layoutParams = LinearLayout.LayoutParams(0, -2, 1f)
        val load =
            page.button("Загрузить из API") {
                local = false
                heading.text = "Внешний API"
                model.load(true)
            }
        val status = page.label("", 15f)
        val scroll = ScrollView(this)
        val rows = column()
        scroll.addView(rows)
        page.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        showRoot(page)
        model.state.observe(this) { s ->
            status.text = s.message
            load.isEnabled = !s.busy
            rows.removeAllViews()
            s.items.forEach { rows.label("${it.id}. ${it.title}\n${it.price} USD", 17f) }
        }
        if (local) model.load(false)
    }

    override fun onSaveInstanceState(out: Bundle) {
        out.putBoolean("local", local)
        super.onSaveInstanceState(out)
    }
}
