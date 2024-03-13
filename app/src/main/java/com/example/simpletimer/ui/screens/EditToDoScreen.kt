package com.example.simpletimer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simpletimer.R
import com.example.simpletimer.model.MainViewModel
import com.example.simpletimer.model.ToDoItem
import com.example.simpletimer.model.datePickerStateToDateString
import com.example.simpletimer.model.dateStringToEpochMillis
import com.example.simpletimer.model.timePickerStateToTimeString
import com.example.simpletimer.model.timeStringToLocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditToDoScreen(
    navController: NavController,
    viewModel: MainViewModel,
) {

    // currentToDo aus dem ViewModel lesen
    val todo = viewModel.currentToDo

    // State Variablen für die OutlinedTextFields
    // vorbelegen mit Infos aus currentToDo
    var title by remember { mutableStateOf(todo.title) }
    var dateString by remember { mutableStateOf(todo.dateString) }
    var timeString by remember { mutableStateOf(todo.timeString) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateStringToEpochMillis(dateString)
    )


    var showTimePicker by remember { mutableStateOf(false) }
    val presetTime = timeStringToLocalTime(timeString)
    var timePickerState by remember {
        mutableStateOf<TimePickerState>(
            TimePickerState(
                initialMinute = presetTime.minute,
                initialHour = presetTime.hour,
                is24Hour = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(id = R.string.enterTitleLabel)) },
            textStyle = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        // Datum Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = dateString,
                readOnly = true,
                onValueChange = {},
                label = { Text(stringResource(id = R.string.enterDateLabel)) },
                textStyle = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Datum")
            }
        }


        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            dateString = datePickerStateToDateString(datePickerState)
                            showDatePicker = false
                        },
                    ) {
                        Text(stringResource(id = R.string.confirmButton))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(id = R.string.dismissButton))
                    }
                }
            )
            {
                DatePicker(state = datePickerState)
            }
        }

        // Zeit Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = timeString,
                readOnly = true,
                onValueChange = {},
                label = { Text(stringResource(id = R.string.enterTimeLabel)) },
                textStyle = MaterialTheme.typography.headlineMedium
            )

            IconButton(onClick = { showTimePicker = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Zeit")
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            timeString = timePickerStateToTimeString(timePickerState)
                            showTimePicker = false
                        },
                    ) {
                        Text(stringResource(id = R.string.confirmButton))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text(stringResource(id = R.string.dismissButton))
                    }
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dismiss Button; gehe zurück zum HomeScreen ohne irgendeine Änderung
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.dismissButton))
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Confirm Button; Füge editiertes ToDo der Liste hinzu. Übernimm die ID
            Button(
                enabled = title.isNotEmpty(),
                onClick = {
                    viewModel.addToList(ToDoItem(
                        id = todo.id,
                        title = title,
                        subject = "$dateString $timeString",
                        timeString = timeString,
                        dateString = dateString)
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.confirmButton))
            }
        }
    }
}
