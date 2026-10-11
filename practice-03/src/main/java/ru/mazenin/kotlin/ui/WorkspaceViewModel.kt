package ru.mazenin.kotlin.ui

import android.app.Application
import androidx.lifecycle.*
import ru.mazenin.kotlin.data.PreferencesRepository
import ru.mazenin.kotlin.domain.*

class WorkspaceViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: WorkspaceRepository = PreferencesRepository(app)
    private val increment = IncrementCounter()
    private val mutable = MutableLiveData(repository.load())
    val state: LiveData<Workspace> = mutable

    fun add() = update(increment(requireNotNull(mutable.value)))

    fun saveNote(note: String) = update(requireNotNull(mutable.value).copy(note = note))

    private fun update(value: Workspace) {
        repository.save(value)
        mutable.value = value
    }
}
