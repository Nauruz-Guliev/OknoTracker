package ru.kpfu.itis.extensions

import dev.icerock.moko.resources.StringResource
import ru.kpfu.itis.utils.Strings

fun Strings.getString(id: StringResource, vararg args: Any): String {
    return this.get(id, args.toList())
}