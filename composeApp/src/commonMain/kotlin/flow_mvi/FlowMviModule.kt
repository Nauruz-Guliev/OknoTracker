package flow_mvi

import org.koin.dsl.module

fun flowMviModule() = module {
    factory<ConfigurationFactory> {
        DefaultConfigurationFactory()
    }
}