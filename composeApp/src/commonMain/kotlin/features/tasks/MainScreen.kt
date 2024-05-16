package features.tasks

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
import androidx.compose.material.ExperimentalMaterialApi
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import features.settings.SettingsTab
import features.statistics.StatisticsTab

class MainScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        var showBottomSheet by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        var taskTitle by rememberSaveable { mutableStateOf("") }
        var taskDescription by rememberSaveable { mutableStateOf("") }

        TabNavigator(AllTasksTab) {

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Tasks") }
                    )
                },
                content = { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        CurrentTab()
                    }
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            sheetState = sheetState
                        ) {
                            TaskBottomSheetContent(
                                title = taskTitle,
                                titleLabel = "Enter task name",
                                onTitleChange = { taskTitle = it },
                                description = taskDescription,
                                descriptionLabel = "Enter task description",
                                onDescriptionChange = { taskDescription = it },
                            )
                        }
                    }
                },
                bottomBar = {
                    BottomNavigation(
                        modifier = Modifier.height(96.dp)
                            .fillMaxWidth(),
                        backgroundColor = MaterialTheme.colorScheme.surfaceDim,
                    ) {

                        Row(
                            modifier = Modifier.width(220.dp)
                                .padding(vertical = 24.dp, horizontal = 16.dp)
                        ) {
                            TabNavigationItem(AllTasksTab)
                            TabNavigationItem(ClosedTasksTab)
                            TabNavigationItem(StatisticsTab)
                            TabNavigationItem(SettingsTab)
                        }

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.End,
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    showBottomSheet = !showBottomSheet
                                },
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
            )
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
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
            BottomNavigationItem(
                selected = isSelected,
                onClick = { tabNavigator.current = tab },
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