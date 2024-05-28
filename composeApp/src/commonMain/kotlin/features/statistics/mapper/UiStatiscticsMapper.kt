package features.statistics.mapper

import features.statistics.UiDayStatistics
import features.statistics.UiStatiscticsEmpty
import features.statistics.UiStatistics
import features.statistics.UiStatisticsState
import ru.kpfu.itis.features.task.domain.model.DayStatistics

fun List<DayStatistics>.mapStatistics(): UiStatisticsState {
    val completedTasks = map(DayStatistics::completedTasksCount).sum()
    val taskCount = map(DayStatistics::tasksCount).sum()
    return if (taskCount == 0) {
        UiStatiscticsEmpty
    } else {
        UiStatistics(
            days = map(::map),
            completedTasksCount = completedTasks,
            completedOnTimeTasksCount = map(DayStatistics::completedOnTimeTasksCount).sum(),
            remainingTasksCount = taskCount - completedTasks
        )
    }
}

private fun map(item: DayStatistics): UiDayStatistics =
    UiDayStatistics(
        date = item.date,
        completedTasksCount = item.completedTasksCount,
        completedOnTimeTasksCount = item.completedOnTimeTasksCount,
        remainingTasksCount = item.remainingTaskCount
    )