package ru.mazenin.kotlin.domain
data class Workspace(val count: Int = 0, val note: String = "")

interface WorkspaceRepository {
    fun load(): Workspace

    fun save(value: Workspace)
}

class IncrementCounter {
    operator fun invoke(value: Workspace) = value.copy(count = value.count + 1)
}
