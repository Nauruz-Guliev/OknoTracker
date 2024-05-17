package ru.kpfu.itis.features.user.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.features.user.data.model.UserAuthRequest
import ru.kpfu.itis.features.user.data.model.UserCreateRequest
import ru.kpfu.itis.features.user.data.model.UserResponse
import ru.kpfu.itis.features.user.data.service.UserService

class UserRepository(
    private val service: UserService,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun signIn(email: String, password: String) = withContext(dispatcher) {
        service.auth(
            UserAuthRequest(
                login = email,
                password = password
            )
        )
    }

    suspend fun signUp(email: String, password: String) = withContext(dispatcher) {
        service.createUser(
            UserCreateRequest(
                email = email,
                password = password
            )
        )
    }

    private fun handleResult(response: UserResponse) {

    }
}