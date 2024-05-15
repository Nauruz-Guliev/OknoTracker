package ru.kpfu.itis.common.data.mapper

import ru.kpfu.itis.common.data.model.ErrorDto
import ru.kpfu.itis.common.domain.model.ErrorModel
import ru.kpfu.itis.shared.MR

class ErrorMapper {

    fun map(
        errorDto: ErrorDto? = null,
        exception: Exception? = null,
    ): ErrorModel {
        return when {
            errorDto != null -> ErrorModel(
                title = errorDto.title,
                details = errorDto.details
            )

            exception != null -> ErrorModel(
                title = exception.message ?: MR.strings.unknown_error_title.toString(),
                details = exception.cause?.message ?: MR.strings.unknown_error_details.toString()
            )

            else -> ErrorModel(
                title = MR.strings.unknown_error_title.toString(),
                details = MR.strings.unknown_error_details.toString()
            )
        }
    }
}