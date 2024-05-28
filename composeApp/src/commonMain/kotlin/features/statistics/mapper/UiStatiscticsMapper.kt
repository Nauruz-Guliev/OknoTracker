package features.statistics.mapper

import features.statistics.UiDayStatistics
import features.statistics.UiStatistics
import ru.kpfu.itis.features.task.domain.model.DayStatistics

fun List<DayStatistics>.mapStatistics(): UiStatistics {
    val completedTasks = map(DayStatistics::completedTasksCount).sum()
    val taskCount = map(DayStatistics::tasksCount).sum()
    return UiStatistics(
        days = map(::map),
        completedTasksCount = completedTasks,
        completedOnTimeTasksCount = map(DayStatistics::completedOnTimeTasksCount).sum(),
        remainingTasksCount = taskCount - completedTasks
    )
}

private fun map(item: DayStatistics): UiDayStatistics =
    UiDayStatistics(
        date = item.date,
        completedTasksCount = item.completedTasksCount,
        completedOnTimeTasksCount = item.completedOnTimeTasksCount,
        remainingTasksCount = item.remainingTaskCount
    )