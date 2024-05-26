package ru.kpfu.itis.features.statistics.mapper
import ru.kpfu.itis.features.task.data.dto.StatisticsResponse
import ru.kpfu.itis.features.task.domain.model.StatisticsModel

class StatisticsMapper {

    fun mapItem(input: StatisticsResponse): StatisticsModel {
        return StatisticsModel(
            tasksCount = input.countOfTasks,
            completedTasksCount = input.countOfCompletedTasks,
            completedOnTimeTasksCount = input.countOfCompletedOnTimeTasks
        )
    }

}