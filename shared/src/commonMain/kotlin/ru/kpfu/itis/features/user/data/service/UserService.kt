package ru.kpfu.itis.features.user.data.service

import dev.icerock.moko.resources.StringResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import ru.kpfu.itis.features.user.data.model.UserAuthRequest
import ru.kpfu.itis.features.user.data.model.UserChangeRequest
import ru.kpfu.itis.features.user.data.model.UserCreateRequest
import ru.kpfu.itis.features.user.data.model.UserResponse
import ru.kpfu.itis.shared.MR
import ru.kpfu.itis.utils.Strings

class UserService(
    private val httpClient: HttpClient,
    private val strings: Strings,
) {

    internal suspend fun changeUser(user: UserChangeRequest): UserResponse {
        return httpClient.put {
            url("${MR.strings.url.get()}/user/")
            setBody(user)
        }.body()
    }

    internal suspend fun createUser(user: UserCreateRequest): UserResponse {
        return httpClient.post {
            url("${MR.strings.url.get()}user/")
            setBody(user)
        }.body()
    }

    internal suspend fun getUser(userId: Long): UserResponse {
        return httpClient.get {
            url("${MR.strings.url.get()}user/$userId")
        }.body()
    }

    internal suspend fun deleteUser(userId: Long): UserResponse {
        return httpClient.delete {
            url("${MR.strings.url.get()}user/$userId")
        }.body()
    }

    internal suspend fun auth(form: UserAuthRequest): UserResponse {
        return httpClient.post {
            url("${MR.strings.url.get()}auth/login")
            header("Content-Type", "application/json")
            setBody(form)
        }.body()
    }

    fun StringResource.get(): String {
        return strings.get(this, emptyList())
    }
}