package ru.mazenin.kotlin

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.mazenin.kotlin.ui.WorkspaceViewModel

abstract class WorkspaceFragment : Fragment() {
    val model: WorkspaceViewModel
        get() = ViewModelProvider(requireActivity())[WorkspaceViewModel::class.java]
}

class HomeFragment : WorkspaceFragment() {
    private lateinit var summary: TextView

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        requireContext().column().apply {
            label("Главная", 24f)
            summary = label("")
            button("Открыть счётчик") { (activity as MainActivity).go(R.id.screen_counter) }
            button("Открыть заметку") { (activity as MainActivity).go(R.id.screen_note) }
        }

    override fun onViewCreated(view: View, state: Bundle?) {
        model.state.observe(viewLifecycleOwner) {
            summary.text = "Нажатий: ${it.count}\nЗаметка: ${it.note.ifEmpty { "не задана" }}"
        }
    }
}

class CounterFragment : WorkspaceFragment() {
    private lateinit var counter: TextView

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        requireContext().column().apply {
            label("Счётчик", 24f)
            counter = label("", 22f)
            button("Увеличить") { model.add() }
            button("К заметке") { (activity as MainActivity).go(R.id.screen_note) }
            button("Назад") { (activity as MainActivity).back() }
        }

    override fun onViewCreated(view: View, state: Bundle?) {
        model.state.observe(viewLifecycleOwner) { counter.text = "Нажатий: ${it.count}" }
    }
}

class NoteFragment : WorkspaceFragment() {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View =
        requireContext().column().apply {
            label("Заметка", 24f)
            val field = input("Текст заметки", model.state.value?.note ?: "")
            val status = label("Данные доступны всем экранам")
            button("Сохранить") {
                model.saveNote(field.text.toString())
                status.text = "Заметка сохранена"
            }
            button("Назад") { (activity as MainActivity).back() }
        }
}
