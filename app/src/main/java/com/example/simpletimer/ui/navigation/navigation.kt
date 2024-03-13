package com.example.simpletimer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.simpletimer.R
import com.example.simpletimer.model.MainViewModel
import com.example.simpletimer.ui.screens.EditToDoScreen
import com.example.simpletimer.ui.screens.HomeScreen
import com.example.simpletimer.ui.screens.TimerScreen

sealed class NavDestination(
    val route: String,
    val title: Int = 0,
    val label: Int = 0,
    val selectedIcon: ImageVector = Icons.Default.Check,
    val unselectedIcon: ImageVector = Icons.Default.Check,
    val showArrowBack: Boolean = false,
    val showFab: Boolean = false,
    val content: @Composable (NavController, MainViewModel) -> Unit
) {
    object Home : NavDestination(
        route = "home",
        title = R.string.homeScreenTitle,
        label = R.string.homeScreenLabel,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        showFab = true,
        content = { navController, viewModel -> HomeScreen(navController, viewModel) }
    )

    object Timer : NavDestination(
        route = "timer",
        title = R.string.timerScreenTitle,
        label = R.string.timerScreenLabel,
        selectedIcon = Icons.Filled.Timelapse,
        unselectedIcon = Icons.Outlined.Timelapse,
        content = { navController, viewModel -> TimerScreen(navController, viewModel) }
    )

    object EditToDo : NavDestination(
        route = "edit_to_do",
        title = R.string.editToDoScreenTitle,
        content = { navController, viewModel -> EditToDoScreen(navController, viewModel) }
    )
}


// Hier alle Bildschirme listen, über die in der Bottom Bar navigiert werden soll
val bottomBarNavDestinations = listOf(
    NavDestination.Home,
    NavDestination.Timer
)

// Hier alle Bildschirme listen, die als FullScreen Bildschirm angesprungen werden sollen
// wenn es keine gibt, dann
// val otherDestinations = emptyList<NavDestination>()
val otherDestinations = listOf(
    NavDestination.EditToDo
)

val navDestinations = bottomBarNavDestinations + otherDestinations

// Hier alle Dialogbilschirme listen
// wenn es keine gibt, dann
// val dialogDestinations = emptyList<NavDestination>()
val dialogDestinations = emptyList<NavDestination>()

