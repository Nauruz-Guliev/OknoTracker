package ru.kpfu.itis.features.settings.data

import ru.kpfu.itis.features.settings.domain.SettingItem
import ru.kpfu.itis.features.settings.domain.SettingKey


fun SettingItem.toDto(): SettingItemDto = SettingItemDto(key = key.name, isChecked = isChecked, title = title)

fun List<SettingItem>.toDtoList(): List<SettingItemDto> = map(SettingItem::toDto)

fun SettingItemDto.toDomain(): SettingItem = SettingItem(key = SettingKey.valueOf(key), isChecked = isChecked, title = title)

fun List<SettingItemDto>.toDomainList(): List<SettingItem> = map(SettingItemDto::toDomain)