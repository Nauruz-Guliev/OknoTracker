package extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import pro.respawn.flowmvi.api.Store

@Composable
fun Store<*, *, *>.startFlowMvi() {
    LaunchedEffect(Unit) { start(this).join() }
}