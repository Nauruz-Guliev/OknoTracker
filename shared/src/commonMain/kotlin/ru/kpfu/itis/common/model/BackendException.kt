package ru.kpfu.itis.common.model

data class BackendException(
    val title: String?,
    val details: String?,
) : Exception(title, Throwable(details))