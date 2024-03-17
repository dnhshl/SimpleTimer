package com.example.simpletimer.ui.navigation

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MyNavBar(
    navController: NavController,
    screens: List<MyNavDestination>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar{
        Log.i(">>>", screens.toString())
        screens.forEach { screen ->
            Log.i(">>>", screen.route)
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        if (currentRoute == screen.route) {
                            screen.selectedIcon
                        }
                        else screen.unselectedIcon,
                        contentDescription = stringResource(screen.title)
                    )
                },
                label = { Text(text = stringResource(screen.label)) },
                alwaysShowLabel = true
            )
        }
    }
}
