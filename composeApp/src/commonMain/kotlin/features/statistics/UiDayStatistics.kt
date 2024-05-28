package features.statistics

data class UiStatistics(
    val days: List<UiDayStatistics>,
    val completedTasksCount: Int,
    val completedOnTimeTasksCount: Int,
    val remainingTasksCount: Int
)

data class UiDayStatistics(
    val date: String,
    val completedTasksCount: Int,
    val completedOnTimeTasksCount: Int,
    val remainingTasksCount: Int
)