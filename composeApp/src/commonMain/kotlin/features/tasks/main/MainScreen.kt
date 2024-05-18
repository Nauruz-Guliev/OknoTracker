package features.tasks.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import extensions.startFlowMvi
import features.settings.SettingsTab
import features.statistics.StatisticsTab
import features.tasks.completed.CompletedTasksTab
import features.tasks.create.TaskBottomSheet
import features.tasks.home.HomeTasksTab
import org.koin.compose.koinInject
import pro.respawn.flowmvi.compose.dsl.subscribe
import ru.kpfu.itis.features.task.domain.model.TaskModel

class MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() = with(koinInject<MainContainer>().store) {

        startFlowMvi()

        var currentTabName by rememberSaveable { mutableStateOf<String?>(null) }
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        var taskModel by rememberSaveable { mutableStateOf<TaskModel?>(null) }

        val state by subscribe { action ->
            when (action) {
                is MainAction.ShowSnackbar -> {

                }

                is MainAction.OpenTaskBottomSheet -> {
                    showBottomSheet = true
                }
            }
        }

        TabNavigator(HomeTasksTab) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentTabName ?: "Tasks") }
                    )
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    BottomActionBar(
                        onTabSelectedAction = {
                            currentTabName = it
                        },
                        onButtonClickedAction = {
                            intent(MainIntent.FloatingButtonClicked)
                        }
                    )
                }
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                TaskBottomSheet(
                    taskDataAction = { model ->
                        taskModel = model
                    }
                )
            }
        }
    }

    @Composable
    private fun BottomActionBar(
        onButtonClickedAction: () -> Unit,
        onTabSelectedAction: (String?) -> Unit
    ) {
        BottomNavigation(
            modifier = Modifier.height(96.dp)
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.surfaceDim,
        ) {

            Row(
                modifier = Modifier.width(220.dp)
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                TabNavigationItem(HomeTasksTab, onTabSelectedAction)
                TabNavigationItem(CompletedTasksTab, onTabSelectedAction)
                TabNavigationItem(StatisticsTab, onTabSelectedAction)
                TabNavigationItem(SettingsTab, onTabSelectedAction)
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                FloatingActionButton(
                    onClick = onButtonClickedAction,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.size(56.dp),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(
        tab: Tab,
        onTabSelected: (String?) -> Unit
    ) {
        val tabNavigator = LocalTabNavigator.current
        val isSelected = tabNavigator.current == tab

        Row(
            modifier = Modifier
                .background(
                    if (isSelected) {
                        Color.LightGray
                    } else {
                        MaterialTheme.colorScheme.surfaceDim
                    },
                    CircleShape
                )
                .padding(4.dp)
                .size(36.dp)
        ) {
            val selectedTabName = tab.options.title
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    tabNavigator.current = tab
                    onTabSelected(selectedTabName)
                },
                icon = {
                    Icon(
                        modifier = Modifier,
                        painter = tab.options.icon ?: rememberVectorPainter(Icons.Filled.Home),
                        contentDescription = tab.options.title,
                    )
                },
                selectedContentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}