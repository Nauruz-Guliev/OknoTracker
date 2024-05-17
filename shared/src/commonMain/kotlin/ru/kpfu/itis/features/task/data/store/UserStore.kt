package ru.kpfu.itis.features.task.data.store

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import okio.Path.Companion.toPath

class UserStore(
    filePath: String
) {

    private val store: KStore<Long> = storeOf("$filePath/user.json".toPath())

    suspend fun getUserId(): Long? {
        return store.get()
    }

    suspend fun setUserID(id: Long) {
        store.set(id)
    }

    suspend fun deleteUserId() {
        store.delete()
    }

    suspend fun updateUserId(id: Long) {
        store.update {
            id
        }
    }

    fun observeUserId(): Flow<Long> {
        return store.updates.filterNotNull()
    }
}