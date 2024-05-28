package features.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import dev.icerock.moko.resources.compose.stringResource
import extensions.startFlowMvi
import features.OTrackerState
import features.statistics.mvi.StatisticsAction
import features.statistics.mvi.StatisticsContainer
import features.statistics.mvi.StatisticsIntent
import org.koin.compose.koinInject
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.OResources
import theme.LocalPaddingValues
import dev.icerock.moko.resources.compose.painterResource as mokoPainterResource

object StatisticsTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(OResources.Statistics.title())
            val icon = rememberVectorPainter(Icons.Outlined.Info)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val store: Store<OTrackerState<UiStatistics>, StatisticsIntent, StatisticsAction> =
            koinInject<StatisticsContainer>().store

        store.startFlowMvi()

        val state by store.subscribe { _ -> }

        when (val currState = state) {
            is OTrackerState.Error -> {
                OErrorScreen(
                    errorModel = currState.error,
                    onClickAction = { store.intent(StatisticsIntent.Outer.TryAgain) },
                    isTryAgain = true
                )
            }

            OTrackerState.Initial -> {
                EmptyStatistics()
            }

            OTrackerState.Loading -> OLoadingScreen()
            is OTrackerState.Success -> Statistics(currState.data)
        }
    }
}

@Composable
private fun EmptyStatistics() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = mokoPainterResource(
                OResources.Statistics.emptyStatisticsImage(),
            ),
            modifier = Modifier.size(200.dp),
            contentDescription = stringResource(OResources.Statistics.emptyStatisticsLabel())
        )
        Text(
            text = stringResource(OResources.Statistics.emptyStatisticsLabel()),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Text(
            text = stringResource(OResources.Statistics.emptyStatiscticsDescription())
        )
    }
}

@Composable
private fun Statistics(
    state: UiStatistics
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(OResources.Statistics.description()),
            style = MaterialTheme.typography.titleMedium,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.size(LocalPaddingValues.current.large))

        Image(
            modifier = Modifier.size(200.dp),
            painter = mokoPainterResource(OResources.Statistics.statisticsImage()),
            contentDescription = OResources.Statistics.statisticsImageContentDescription()
                .toString()
        )

        Spacer(modifier = Modifier.size(LocalPaddingValues.current.large))

        Row(
            horizontalArrangement = Arrangement.Center,
        ) {
            StatisticItem(
                label = stringResource(OResources.Statistics.completedTasks()),
                value = state.completedTasksCount.toString(),
            )

            Spacer(modifier = Modifier.height(LocalPaddingValues.current.medium))

            StatisticItem(
                label = stringResource(OResources.Statistics.completedOnTimeTasks()),
                value = state.completedOnTimeTasksCount.toString(),
            )

            Spacer(modifier = Modifier.height(LocalPaddingValues.current.medium))

            StatisticItem(
                label = stringResource(OResources.Statistics.remainingTasks()),
                value = state.remainingTasksCount.toString()
            )
        }
    }
}

@Composable
private fun RowScope.StatisticItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
        )
        Spacer(modifier = Modifier.height(LocalPaddingValues.current.extraSmall))
        Text(
            text = label,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(LocalPaddingValues.current.small)
        )
    }
}