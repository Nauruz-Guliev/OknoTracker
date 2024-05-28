package ru.kpfu.itis.common.mapper

import dev.icerock.moko.resources.StringResource
import ru.kpfu.itis.common.model.ErrorDto
import ru.kpfu.itis.common.model.ErrorModel
import ru.kpfu.itis.shared.MR
import ru.kpfu.itis.utils.Strings

class ErrorMapper(
    private val strings: Strings
) {

    fun map(
        errorDto: ErrorDto? = null,
        exception: Throwable? = null,
    ): ErrorModel {
        return when {
            errorDto != null -> ErrorModel(
                title = errorDto.title,
                details = errorDto.details
            )

            exception != null -> ErrorModel(
                title = exception.message ?: MR.strings.unknown_error_title.get(),
                details = exception.cause?.message ?: MR.strings.unknown_error_details.get()
            ).also {
                println(exception.message)
            }

            else -> ErrorModel(
                title = MR.strings.unknown_error_title.get(),
                details = MR.strings.unknown_error_details.get()
            )
        }
    }

    fun StringResource.get(): String {
        return strings.get(this, emptyList())
    }
}