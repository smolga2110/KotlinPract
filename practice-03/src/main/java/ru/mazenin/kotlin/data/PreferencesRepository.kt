package ru.mazenin.kotlin.data

import android.content.Context
import ru.mazenin.kotlin.domain.*

class PreferencesRepository(context: Context) : WorkspaceRepository {
    private val prefs = context.getSharedPreferences("workspace", 0)

    override fun load() = Workspace(prefs.getInt("count", 0), prefs.getString("note", "") ?: "")

    override fun save(value: Workspace) {
        prefs.edit().putInt("count", value.count).putString("note", value.note).apply()
    }
}
