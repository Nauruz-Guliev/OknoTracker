package ru.kpfu.itis.features.task.data.store

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath

class UserStore(
    filePath: String,
    private val dispatcher: CoroutineDispatcher
) {

    private val store: KStore<Long> = storeOf("$filePath/user.json".toPath())

    suspend fun getUserId(): Long? = withContext(dispatcher) {
        store.get()
    }

    suspend fun setUserID(id: Long) = withContext(dispatcher) {
        store.set(id)
    }

    suspend fun deleteUserId() = withContext(dispatcher) {
        store.delete()
    }

    suspend fun updateUserId(id: Long) = withContext(dispatcher) {
        store.update {
            id
        }
    }

    fun observeUserId(): Flow<Long> {
        return store.updates.filterNotNull()
    }
}