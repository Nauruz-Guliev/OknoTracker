package features.statistics

import androidx.compose.runtime.Immutable

@Immutable
data class StatisticsState(
    val completedTasksCount: Int,
    val completedOnTimeTasksPercent: Int,
    val remainingTasksCount: Int
)