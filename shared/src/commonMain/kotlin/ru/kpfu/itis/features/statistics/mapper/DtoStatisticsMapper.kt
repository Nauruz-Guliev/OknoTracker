package ru.kpfu.itis.features.statistics.mapper

import ru.kpfu.itis.common.mapper.Mapper
import ru.kpfu.itis.features.task.data.dto.TaskStatisticDto
import ru.kpfu.itis.features.task.data.dto.TaskStatisticsResponse
import ru.kpfu.itis.features.task.domain.model.DayStatistics

class DtoStatisticsMapper : Mapper<TaskStatisticDto, DayStatistics> {

    fun map(input: TaskStatisticsResponse): List<DayStatistics> {
        if (input.error != null) {
            throw mapToException(input.error)
        }
        return input.data?.map(::mapItem) ?: emptyList()
    }

    override fun mapItem(input: TaskStatisticDto): DayStatistics {
        return DayStatistics(
            date = input.date,
            tasksCount = input.countOfTasks.toInt(),
            completedTasksCount = input.countOfCompletedTasks.toInt(),
            completedOnTimeTasksCount = input.countOfCompletedOnTimeTasks.toInt()
        )
    }

}