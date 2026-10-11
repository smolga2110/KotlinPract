package ru.mazenin.kotlin

import android.os.Bundle
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.time.LocalDate
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("expenses", MODE_PRIVATE)
        val saved = JSONArray(prefs.getString("items", "[]"))
        val tracker =
            ExpenseTracker(
                (0 until saved.length()).map { i ->
                    val x = saved.getJSONObject(i)
                    Expense(
                        BigDecimal(x.getString("amount")),
                        x.getString("category"),
                        LocalDate.parse(x.getString("date")),
                    )
                }
            )
        val page = column()
        page.label("Личные расходы", 26f)
        page.label("Практическая работа 1 · Kotlin", 14f)
        val amount = page.input("Сумма, ₽")
        amount.inputType = 8194
        val category = page.input("Категория")
        val date = page.input("Дата ГГГГ-ММ-ДД", LocalDate.now().toString())
        val status = page.label("Добавьте расход")
        lateinit var output: android.widget.TextView
        fun refresh() {
            output.text =
                tracker.displayAll() +
                    "\n\nИТОГИ ПО КАТЕГОРИЯМ\n" +
                    tracker.totalsByCategory().entries.joinToString("\n") {
                        "${it.key}: ${it.value.setScale(2)} ₽"
                    }
        }
        page.button("Добавить расход") {
            try {
                val value =
                    BigDecimal(amount.text.toString().replace(',', '.'))
                        .setScale(2, java.math.RoundingMode.UNNECESSARY)
                tracker.add(
                    Expense(value, category.text.toString().trim(), LocalDate.parse(date.text))
                )
                val data = JSONArray()
                tracker.all().forEach {
                    data.put(
                        JSONObject()
                            .put("amount", it.amount.toPlainString())
                            .put("category", it.category)
                            .put("date", it.date.toString())
                    )
                }
                prefs.edit().putString("items", data.toString()).apply()
                amount.text.clear()
                status.text = "Расход сохранён"
                refresh()
            } catch (e: Exception) {
                status.text = "Проверьте сумму, категорию и дату"
            }
        }
        val scroll = ScrollView(this)
        output =
            android.widget.TextView(this).apply {
                textSize = 17f
                setPadding(0, 16, 0, 8)
            }
        scroll.addView(output)
        page.addView(scroll, android.widget.LinearLayout.LayoutParams(-1, 0, 1f))
        refresh()
        showRoot(page)
    }
}
