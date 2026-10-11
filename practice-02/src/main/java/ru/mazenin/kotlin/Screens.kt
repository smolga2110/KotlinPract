package ru.mazenin.kotlin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        requireContext().column().apply {
            label("Главная", 24f)
            label("Учебное приложение с тремя фрагментами. Выберите действие.")
            button("Открыть счётчик") { (activity as MainActivity).go(R.id.screen_counter) }
            button("Открыть заметку") { (activity as MainActivity).go(R.id.screen_note) }
        }
}

class CounterFragment : Fragment() {
    private var count = 0

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        count = state?.getInt("count") ?: count
        return requireContext().column().apply {
            label("Счётчик", 24f)
            val counter = label("Нажатий: $count", 22f)
            button("Увеличить") {
                count++
                counter.text = "Нажатий: $count"
            }
            button("К заметке") { (activity as MainActivity).go(R.id.screen_note) }
            button("Назад") { (activity as MainActivity).back() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("count", count)
        super.onSaveInstanceState(outState)
    }
}

class NoteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        requireContext().column().apply {
            val prefs = requireContext().getSharedPreferences("notes", 0)
            label("Заметка", 24f)
            val field = input("Текст заметки", prefs.getString("note", "") ?: "")
            val status = label("Заметка хранится на устройстве")
            button("Сохранить") {
                prefs.edit().putString("note", field.text.toString()).apply()
                status.text = "Заметка сохранена"
            }
            button("Назад") { (activity as MainActivity).back() }
        }
}
