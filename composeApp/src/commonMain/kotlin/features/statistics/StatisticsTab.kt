package features.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import base.BaseScreen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import design_system.screens.OErrorScreen
import design_system.screens.OLoadingScreen
import dev.icerock.moko.resources.compose.stringResource
import features.OTrackerState
import features.statistics.mvi.StatisticsAction
import features.statistics.mvi.StatisticsContainer
import features.statistics.mvi.StatisticsIntent
import ru.kpfu.itis.OResources
import theme.LocalPaddingValues
import dev.icerock.moko.resources.compose.painterResource as mokoPainterResource

object StatisticsTab :
    BaseScreen<OTrackerState<UiStatisticsState>, StatisticsIntent, StatisticsAction>(), Tab {
    @Composable
    override fun Content() = withStore<StatisticsContainer> {
        currentState = { state ->
            when (state) {
                is OTrackerState.Error -> OErrorScreen(
                    errorModel = state.error,
                    onClickAction = { intent(StatisticsIntent.Outer.TryAgain) },
                    isTryAgain = true
                )

                OTrackerState.Initial -> {
                    EmptyStatistics()
                }

                OTrackerState.Loading -> OLoadingScreen()
                is OTrackerState.Success -> when (state.data) {
                    UiStatiscticsEmpty -> EmptyStatistics()
                    is UiStatistics -> Statistics(state.data)
                }
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
                text = stringResource(OResources.Statistics.emptyStatisticsDescription())
            )
        }
    }
    @Composable
    private fun Statistics(
        state: UiStatistics,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalPaddingValues.current.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            StatisticsBlock(state)
            Spacer(modifier = Modifier.size(LocalPaddingValues.current.extraLarge))
            StatisticsChart(state)
        }
    }
    @Composable
    private fun StatisticsChart(state: UiStatistics) {
        val chartParameters = listOf(
            BarParameters(
                dataName = stringResource(OResources.Statistics.completedTasks()),
                data = state.days.map(UiDayStatistics::completedTasksCount).map(Int::toDouble),
                barColor = MaterialTheme.colorScheme.primary
            ),
            BarParameters(
                dataName = stringResource(OResources.Statistics.completedOnTimeTasks()),
                data = state.days.map(UiDayStatistics::completedOnTimeTasksCount)
                    .map(Int::toDouble),
                barColor = MaterialTheme.colorScheme.outline
            ),
            BarParameters(
                dataName = stringResource(OResources.Statistics.remainingTasks()),
                data = state.days.map(UiDayStatistics::remainingTasksCount).map(Int::toDouble),
                barColor = MaterialTheme.colorScheme.secondary
            ),
        )
        Box(Modifier.fillMaxWidth()) {
            BarChart(
                chartParameters = chartParameters,
                gridColor = Color.DarkGray,
                xAxisData = state.days.map(UiDayStatistics::date),
                isShowGrid = true,
                animateChart = true,
                showGridWithSpacer = true,
                yAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                ),
                xAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 15,
                barWidth = 20.dp,
                barCornerRadius = 5.dp
            )
        }
    }
    @Composable
    private fun StatisticsBlock(state: UiStatistics) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(size = 10.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .padding(LocalPaddingValues.current.medium),
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

            Spacer(modifier = Modifier.height(LocalPaddingValues.current.medium))

            Image(
                modifier = Modifier.size(100.dp),
                painter = mokoPainterResource(OResources.Statistics.statisticsImage()),
                contentDescription = OResources.Statistics.statisticsImageContentDescription()
                    .toString()
            )
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
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(LocalPaddingValues.current.extraSmall))
            Text(
                text = label,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
            )
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(OResources.Statistics.title())
            val icon = rememberVectorPainter(Icons.Filled.DateRange)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }
}