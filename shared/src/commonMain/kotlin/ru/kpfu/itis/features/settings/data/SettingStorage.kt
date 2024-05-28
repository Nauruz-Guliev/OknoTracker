package ru.kpfu.itis.features.settings.data

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath
import ru.kpfu.itis.features.settings.domain.SettingItem

interface SettingStorage {
    suspend fun updateSetting(item: SettingItem): List<SettingItem>
    suspend fun save(list: List<SettingItem>)
    suspend fun getAll(): List<SettingItem>
}

private class SettingStorageImpl(private val store: KStore<List<SettingItemDto>>) : SettingStorage {
    override suspend fun updateSetting(item: SettingItem): List<SettingItem> {
        store.update { saved ->
            if (saved == null) return@update saved
            saved.forEachIndexed { index, savedItem ->
                if (savedItem.key == item.key.name) {
                    return@update saved.toMutableList().apply { set(index, item.toDto()) }
                }
            }
            saved
        }
        return getAll()
    }

    override suspend fun save(list: List<SettingItem>) {
        store.set(list.toDtoList())
    }

    override suspend fun getAll(): List<SettingItem> {
        return store.get()?.toDomainList() ?: emptyList()
    }

}

fun SettingStorage(baseFilePath: String): SettingStorage =
    SettingStorageImpl(storeOf("$baseFilePath/settings.json".toPath()))