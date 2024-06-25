package com.example.todoapp3.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoapp3.R
import com.example.todoapp3.model.Importance
import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.navigation.MainDestinations
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.example.todoapp3.view.general.MyDivider
import com.example.todoapp3.view.general.PrimaryBodyText
import com.example.todoapp3.viewModels.TodoItemsViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(todoItemsViewModel: TodoItemsViewModel, navController: NavController) {
    val todoItem: State<TodoItem?> = todoItemsViewModel.curItem.observeAsState()
    val text = remember { mutableStateOf(todoItem.value?.text ?: "") }
    val importance = remember { mutableStateOf(todoItem.value?.importance ?: Importance.MEDIUM) }
    val pickedDate = remember { mutableStateOf(todoItem.value?.deadline ?: LocalDate.now()) }
    val scrollState = rememberScrollState()

    ToDoApp3Theme {
        Scaffold(
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = if (scrollState.value > 0) 6.dp else 0.dp,
                            shape = RoundedCornerShape(0.dp)
                        )
                ) {
                    TopAppBar(
                        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                        navigationIcon = {
                            Image(
                                painter = painterResource(R.drawable.delete),
                                contentDescription = stringResource(R.string.delete_task),
                                modifier = Modifier
                                    .padding(start = 26.dp, top = 6.dp)
                                    .clickable {
                                        if (todoItemsViewModel.curItem.value != null) {
                                            todoItemsViewModel.deleteItemById(
                                                todoItemsViewModel.curItem.value?.id ?: ""
                                            )
                                        }
                                        navController.navigate(MainDestinations.HOME_LIST)
                                    },
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        },
                        title = {},
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        actions = {
                            Text(
                                text = stringResource(R.string.save),
                                modifier = Modifier
                                    .padding(end = 26.dp, top = 6.dp)
                                    .clickable {
                                        if (todoItemsViewModel.curItem.value != null) {
                                            todoItemsViewModel.editItem(
                                                text = text.value,
                                                importance = importance.value,
                                                deadline = pickedDate.value,
                                                false,
                                                LocalDate.now(),
                                            )
                                        } else {
                                            todoItemsViewModel.addNewItem(
                                                TodoItem(
                                                    id = "${Random.nextLong(21, 10_000_000_000)}",
                                                    text = text.value,
                                                    importance = importance.value,
                                                    deadline = pickedDate.value,
                                                    false,
                                                    LocalDate.now(),
                                                    null
                                                )
                                            )
                                        }
                                        navController.navigate(MainDestinations.HOME_LIST)
                                    },
                                color = MaterialTheme.colorScheme.tertiary,
                            )
                        },
                    )

                }

            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(100.dp, 1000.dp)
                        .padding(horizontal = 22.dp)
                        .shadow(2.dp, shape = RoundedCornerShape(12.dp), clip = false)
                        .padding(2.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = MaterialTheme.colorScheme.background,
                        focusedBorderColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = MaterialTheme.colorScheme.background,
                        disabledBorderColor = MaterialTheme.colorScheme.background,
                        cursorColor = MaterialTheme.colorScheme.outlineVariant,

                        ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.what_needs_to_be_done),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                )
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                val expanded = remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 26.dp)
                ) {
                    PrimaryBodyText(
                        text = stringResource(R.string.importance),
                        modifier = Modifier
                            .clickable {
                                expanded.value = true
                            },

                        )

                    DropdownMenu(
                        modifier = Modifier.padding(start = 10.dp),
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                    ) {
                        (Importance.values()).forEach { curImportance ->
                            DropdownMenuItem(
                                onClick = {
                                    when (curImportance) {
                                        Importance.LOW -> {
                                            importance.value = Importance.LOW
                                        }

                                        Importance.MEDIUM -> {
                                            importance.value = Importance.MEDIUM
                                        }

                                        Importance.HIGH -> {
                                            importance.value = Importance.HIGH
                                        }
                                    }
                                    expanded.value = false
                                },
                                text = { Text(curImportance.text) }
                            )
                        }
                    }
                }
                Text(
                    text = if (importance.value == Importance.HIGH) "!!${importance.value.text}"
                    else importance.value.text,
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 2.dp)
                        .clickable {
                            expanded.value = true
                        },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (importance.value == Importance.LOW) MaterialTheme.colorScheme.tertiary
                    else if (importance.value == Importance.HIGH) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onTertiary
                    }
                )
                MyDivider(modifier = Modifier.padding(vertical = 20.dp, horizontal = 26.dp))
                val deadlineIsSelected =
                    remember { mutableStateOf(todoItem.value?.deadline != null) }
                val isChecked = remember { mutableStateOf(todoItem.value?.deadline != null) }
                val formattedDate = remember {
                    derivedStateOf {
                        DateTimeFormatter
                            .ofPattern("MMM dd yyyy")
                            .format(pickedDate.value)
                    }
                }
                val dateDialogState = rememberMaterialDialogState()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 26.dp)
                    ) {
                        PrimaryBodyText(
                            text = "Сделать до",
                            modifier = Modifier.clickable {
                                dateDialogState.show()
                            }
                        )
                        if (deadlineIsSelected.value && isChecked.value) {
                            Text(
                                text = formattedDate.value,
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.clickable {
                                    dateDialogState.show()
                                }
                            )
                        }
                    }
                    Switch(
                        modifier = Modifier.padding(end = 40.dp),
                        checked = isChecked.value,
                        onCheckedChange = {
                            isChecked.value = !isChecked.value
                            if (isChecked.value) {
                                dateDialogState.show()
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                MyDivider(modifier = Modifier.padding(vertical = 10.dp, horizontal = 26.dp))
                Text(
                    text = stringResource(R.string.delete_task),
                    modifier = Modifier
                        .padding(top = 20.dp, start = 60.dp, bottom = 70.dp)
                        .clickable {
                            if (todoItemsViewModel.curItem.value != null) {
                                todoItemsViewModel.deleteItemById(
                                    todoItemsViewModel.curItem.value?.id ?: ""
                                )
                            }
                            navController.navigate(MainDestinations.HOME_LIST)
                        },
                    color = if (text.value.isEmpty()) MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.error
                )
                MaterialDialog(
                    dialogState = dateDialogState,
                    buttons = {
                        positiveButton(
                            text = stringResource(R.string.ready),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                        ) {
                            deadlineIsSelected.value = true
                        }
                        negativeButton(
                            text = stringResource(R.string.cancel),
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                        ) {
                            if (!deadlineIsSelected.value) {
                                isChecked.value = false
                            }
                        }
                    }
                ) {
                    datepicker(
                        initialDate = LocalDate.now(),
                        title = DateTimeFormatter
                            .ofPattern("  yyyy")
                            .format(LocalDate.now()),
                        colors = DatePickerDefaults.colors(
                            headerBackgroundColor = MaterialTheme.colorScheme.tertiary,
                            dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,

                            )
                    ) {
                        pickedDate.value = it
                    }
                }
            }
        }
    }
}
