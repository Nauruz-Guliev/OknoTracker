package features.statistics


sealed interface UiStatisticsState

data class UiStatistics(
    val days: List<UiDayStatistics>,
    val completedTasksCount: Int,
    val completedOnTimeTasksCount: Int,
    val remainingTasksCount: Int
) : UiStatisticsState

data object UiStatiscticsEmpty : UiStatisticsState

data class UiDayStatistics(
    val date: String,
    val completedTasksCount: Int,
    val completedOnTimeTasksCount: Int,
    val remainingTasksCount: Int
)