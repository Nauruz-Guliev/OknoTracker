package features.settings

import ru.kpfu.itis.features.settings.domain.SettingItem

fun List<SettingItem>.toUi(): List<UiSettingItem> = map { item ->
    UiSettingItem(
        key = item.key,
        title = item.title,
        isChecked = item.isChecked
    )
}

fun UiSettingItem.toDomain() = SettingItem(key = key, isChecked = isChecked, title = title)