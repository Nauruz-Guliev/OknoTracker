package ru.kpfu.itis.common.model

class AuthException(
    message: String = "You need to be authenticated"
) : Exception(message)