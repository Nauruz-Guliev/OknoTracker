package ru.kpfu.itis.di

import org.koin.dsl.module
import ru.kpfu.itis.features.attachments.mapper.AttachmentMapper
import ru.kpfu.itis.features.task.data.mapper.TaskMapper

fun mapperModule() = module {
    factory {
        TaskMapper()
    }
    factory {
        AttachmentMapper()
    }
}