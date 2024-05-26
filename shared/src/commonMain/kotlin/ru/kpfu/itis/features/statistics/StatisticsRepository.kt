package ru.kpfu.itis.features.statistics

import ru.kpfu.itis.features.statistics.mapper.StatisticsMapper
import ru.kpfu.itis.features.task.data.service.TaskService
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.StatisticsModel

interface StatisticsRepository {
    suspend fun getStatistics(): StatisticsModel
}

private class StatisticsRepositoryImpl(
    private val statisticsService: TaskService,
    private val mapper: StatisticsMapper,
    private val userStore: UserStore
) : StatisticsRepository {

    override suspend fun getStatistics(): StatisticsModel {
        return statisticsService.getStatistics(
            userId = userStore.getUserId() ?: error("Not authorized"),
        ).let(mapper::mapItem)
    }

}

fun StatisticsRepository(
    statisticsService: TaskService,
    mapper: StatisticsMapper,
    userStore: UserStore
): StatisticsRepository = StatisticsRepositoryImpl(statisticsService, mapper, userStore)