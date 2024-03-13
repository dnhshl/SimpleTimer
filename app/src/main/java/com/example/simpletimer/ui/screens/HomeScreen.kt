package com.example.simpletimer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simpletimer.R
import com.example.simpletimer.model.MainViewModel
import com.example.simpletimer.model.ToDoItem
import com.example.simpletimer.ui.navigation.NavDestination


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel
) {

    val toDoList by viewModel.toDoList.collectAsState(initial = emptyList<ToDoItem>())

    val selectedToDo by viewModel.selectedToDo.collectAsState(ToDoItem())


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Liste befüllen mit den Elementen der unserer ToDo Liste
            items(toDoList) {todo ->
                ListItemCard(
                    todo = todo,
                    isSelected = selectedToDo.id == todo.id,
                    onItemClick = { viewModel.setSelectedToDo(todo) },
                    onDeleteClick = { viewModel.deleteFromList(todo.id) },
                    onEditClick = {
                        viewModel.setCurrentToDo(todo)
                        navController.navigate(NavDestination.EditToDo.route)
                    }
                )
            }
        }
    }
}


// Composable for a single list item
@Composable
fun ListItemCard(
    todo: ToDoItem,
    isSelected: Boolean = false,
    onItemClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    // Menu State
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onItemClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick() }
                .background(if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = todo.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = todo.subject, style = MaterialTheme.typography.bodySmall)
            }

            // Dropdown Menu Button
            Box { // To position the DropdownMenu correctly
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More Options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(id = R.string.deleteLabel))
                            }
                        },
                        onClick = {
                            expanded = false
                            onDeleteClick()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(id = R.string.editLabel))
                            }
                        },
                        onClick = {
                            expanded = false
                            onEditClick()
                        }
                    )
                }
            }

        }
    }
}





