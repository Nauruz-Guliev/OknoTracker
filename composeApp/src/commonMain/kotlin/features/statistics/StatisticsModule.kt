package features.statistics

import org.koin.dsl.module
import ru.kpfu.itis.features.statistics.StatisticsRepository
import ru.kpfu.itis.features.statistics.mapper.StatisticsMapper

fun statisticsModule() = module {
    factory {
        StatisticsMapper()
    }

    single {
        StatisticsContainer(
            userStore = get(),
            configurationFactory = get(),
            errorMapper = get()
        )
    }

    single {
        StatisticsRepository(
            statisticsService = get(),
            mapper = get(),
            userStore = get()
        )
    }
}