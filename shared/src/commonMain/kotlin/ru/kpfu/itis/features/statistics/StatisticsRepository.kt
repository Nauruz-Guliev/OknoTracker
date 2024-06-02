package ru.kpfu.itis.features.statistics

import ru.kpfu.itis.features.statistics.mapper.DtoStatisticsMapper
import ru.kpfu.itis.features.task.data.service.TaskService
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.DayStatistics

interface StatisticsRepository {
    suspend fun getStatistics(): List<DayStatistics>
}

private class StatisticsRepositoryImpl(
    private val statisticsService: TaskService,
    private val mapper: DtoStatisticsMapper,
    private val userStore: UserStore
) : StatisticsRepository {

    override suspend fun getStatistics(): List<DayStatistics> {
        return statisticsService.getStatistics(
            userId = userStore.getUserId() ?: error("Not authorized"),
        ).let(mapper::map)
    }
}

fun StatisticsRepository(
    statisticsService: TaskService,
    mapper: DtoStatisticsMapper,
    userStore: UserStore
): StatisticsRepository = StatisticsRepositoryImpl(statisticsService, mapper, userStore)