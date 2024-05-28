package features.statistics.di

import features.statistics.mvi.StatisticsContainer
import org.koin.dsl.module
import ru.kpfu.itis.features.statistics.mapper.DtoStatisticsMapper

fun statisticsModule() = module {
    factory {
        DtoStatisticsMapper()
    }

    single {
        StatisticsContainer(
            configurationFactory = get(),
            errorMapper = get(),
            repository = get()
        )
    }
}