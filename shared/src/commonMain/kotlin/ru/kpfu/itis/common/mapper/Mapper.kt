package ru.kpfu.itis.common.mapper

import ru.kpfu.itis.common.model.BackendException
import ru.kpfu.itis.common.model.ErrorDto

interface Mapper<Input, Output> {

    fun mapItem(input: Input): Output

    fun mapList(input: List<Input>): List<Output> = input.map(this::mapItem)

    fun mapToException(errorDto: ErrorDto?) = BackendException(
        title = errorDto?.title,
        details = errorDto?.details
    )
}