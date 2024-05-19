package ru.kpfu.itis.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.kpfu.itis.features.task.data.store.UserStore

fun networkModule() = module {
    single {
        val userStore = get<UserStore>()
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
        }.apply {
            plugin(HttpSend).intercept { request ->
                val userId = userStore.getUserId()
                withContext(Dispatchers.IO) {
                    request.headers.apply {
                        append("Content-Type", "application/json")
                        userId?.let { append("Auth", "${userId}") }
                    }
                    execute(request)
                }
            }
        }
    }
}