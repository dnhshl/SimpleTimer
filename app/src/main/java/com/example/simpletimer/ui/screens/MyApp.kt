package com.example.simpletimer.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.simpletimer.model.MainViewModel
import com.example.simpletimer.model.ToDoItem
import com.example.simpletimer.ui.navigation.MyMenu
import com.example.simpletimer.ui.navigation.MyNavBar
import com.example.simpletimer.ui.navigation.MyNavDestination
import com.example.simpletimer.ui.navigation.MyTopBar
import com.example.simpletimer.ui.navigation.bottomBarNavDestinations
import com.example.simpletimer.ui.navigation.dialogDestinations
import com.example.simpletimer.ui.navigation.navDestinations
import kotlinx.coroutines.launch

@Composable
fun MyApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val currentScreen = navDestinations.firstOrNull { it.route == currentRoute }

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            if (currentScreen?.showFab ?: false) {
                FloatingActionButton(
                    onClick = {
                        viewModel.setCurrentToDo(ToDoItem())
                        navController.navigate(MyNavDestination.EditToDo.route)
//                        val date = randomDateString()
//                        val time= randomTimeString()
//                        viewModel.addToList(
//                            ToDoItem(
//                                title = randomTitle(),
//                                subject = "$date $time",
//                                dateString = date,
//                                timeString = time
//                            )
//                        )
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) {
            Snackbar(
                snackbarData = it,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                actionColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dismissActionContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } },
        topBar = {
            MyTopBar(
                navController = navController,
                screens = navDestinations,
                onMenuClick = { showMenu = !showMenu },
            )
        },
        bottomBar = {
            if (bottomBarNavDestinations.any { it.route == currentRoute }) {
                MyNavBar(
                    navController = navController,
                    screens = bottomBarNavDestinations
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MyNavDestination.Home.route,
            modifier = Modifier
                .padding(paddingValues)
        ) {
            // Screens via BottomBar und Fullscreens
            navDestinations.forEach { screen ->
                composable(screen.route) {
                    screen.content(navController, viewModel)
                }
            }
            // Dialog Screens
            dialogDestinations.forEach { screen ->
                dialog(screen.route) {
                    screen.content(navController, viewModel)
                }
            }
        }

        // Observe SnackbarState
        LaunchedEffect(key1 = viewModel.snackbarState) {
            viewModel.snackbarState.collect { snackbarState ->
                snackbarState?.let {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = it.duration,
                            withDismissAction = true
                        )
                        // Clear the Snackbar
                        viewModel.dismissSnackbar()
                    }
                }
            }
        }


        MyMenu(
            showMenu = showMenu,
            navController = navController,
            paddingValues = paddingValues,
            onToggleMenu = { showMenu = !showMenu }
        )

    }

}

