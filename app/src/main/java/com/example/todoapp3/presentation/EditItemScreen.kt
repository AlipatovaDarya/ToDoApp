package com.example.todoapp3.presentation

import android.content.res.Configuration
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.R
import com.example.todoapp3.data.room.entity.TodoItem
import com.example.todoapp3.presentation.common.MyDivider
import com.example.todoapp3.presentation.common.PrimaryBodyText
import com.example.todoapp3.presentation.common.SnackBar
import com.example.todoapp3.presentation.common.showErrorSnackbar
import com.example.todoapp3.presentation.model.Importance
import com.example.todoapp3.presentation.utils.formatDate
import com.example.todoapp3.presentation.utils.formatYear
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.util.UUID


/**
 * Composable function for show part of editing item
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    uiState: EditTodoUiState,
    navController: NavController,
    itemId: String,
    getTodoById: (String) -> Unit,
    insertTodo: (TodoItem) -> Unit,
    deleteTodoById: (String) -> Unit,
    removeError: () -> Unit,
    syncRemote: () -> Unit
) {
    val text = remember { mutableStateOf(uiState.curTodoItem?.text ?: "") }
    val importance =
        remember { mutableStateOf(uiState.curTodoItem?.importance ?: Importance.BASIC) }
    val pickedDate = remember { mutableStateOf(uiState.curTodoItem?.deadline) }
    val expanded = remember { mutableStateOf(false) }
    val deadlineIsSelected = remember { mutableStateOf(uiState.curTodoItem?.deadline != null) }
    val isChecked = remember { mutableStateOf(uiState.curTodoItem?.deadline != null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarDateVisible = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current


    LaunchedEffect(itemId) {
        getTodoById(itemId)
    }


    LaunchedEffect(uiState.curTodoItem) {
        text.value = uiState.curTodoItem?.text ?: ""
        importance.value = uiState.curTodoItem?.importance ?: Importance.BASIC
        isChecked.value = uiState.curTodoItem?.deadline != null
        deadlineIsSelected.value = uiState.curTodoItem?.deadline != null
        pickedDate.value = uiState.curTodoItem?.deadline
    }

    uiState.errorCode?.let {
        LaunchedEffect(it) {
            showErrorSnackbar(context = context, it, snackbarHostState, scope, syncRemote)
            removeError()
        }
    }


    ToDoApp3Theme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            containerColor = MaterialTheme.colorScheme.surface,
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
                                painter = painterResource(R.drawable.close),
                                contentDescription = stringResource(R.string.close_task),
                                modifier = Modifier
                                    .padding(start = 26.dp, top = 6.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    },
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        },
                        title = {},
                        actions = {
                            Text(
                                text = stringResource(R.string.save),
                                modifier = Modifier
                                    .padding(end = 26.dp, top = 6.dp)
                                    .clickable {
                                        if (text.value
                                                .trim()
                                                .isNotEmpty()
                                        ) {
                                            if (uiState.curTodoItem != null) {
                                                insertTodo(
                                                    uiState.curTodoItem.copy(
                                                        text = text.value,
                                                        importance = importance.value,
                                                        deadline = pickedDate.value,
                                                        modifiedAt = LocalDate.now(),
                                                        isSynced = true,
                                                        isModified = false
                                                    )
                                                )
                                            } else {
                                                insertTodo(
                                                    TodoItem(
                                                        id = UUID
                                                            .randomUUID()
                                                            .toString(),
                                                        text = text.value,
                                                        importance = importance.value,
                                                        deadline = pickedDate.value,
                                                        isCompleted = false,
                                                        createdAt = LocalDate.now(),
                                                        modifiedAt = LocalDate.now(),
                                                        isSynced = false,
                                                        isModified = false,
                                                        isDeleted = false
                                                    )
                                                )
                                            }
                                        }
                                        navController.popBackStack()
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
                OutlinedTextField(value = text.value,
                    onValueChange = {
                        text.value = it
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(100.dp, Int.MAX_VALUE.dp)
                        .padding(horizontal = 22.dp)
                        .shadow(
                            2.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false,
                            spotColor = MaterialTheme.colorScheme.outline
                        )
                        .padding(2.dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.what_needs_to_be_done),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    })
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                Column(
                    modifier = Modifier.padding(horizontal = 26.dp)
                ) {
                    PrimaryBodyText(
                        text = stringResource(R.string.importance),
                        modifier = Modifier.clickable {
                            expanded.value = true
                        },
                    )
                    DropdownMenu(
                        modifier = Modifier.padding(start = 10.dp),
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                    ) {
                        (Importance.values()).forEach { curImportance ->
                            DropdownMenuItem(onClick = {
                                when (curImportance) {
                                    Importance.LOW -> {
                                        importance.value = Importance.LOW
                                    }

                                    Importance.BASIC -> {
                                        importance.value = Importance.BASIC
                                    }

                                    Importance.IMPORTANT -> {
                                        importance.value = Importance.IMPORTANT
                                    }
                                }
                                expanded.value = false
                            }, text = { Text(curImportance.text) })
                        }
                    }
                }
                Text(
                    text = if (importance.value == Importance.IMPORTANT) "!!${importance.value.text}"
                    else importance.value.text,
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 2.dp)
                        .clickable {
                            expanded.value = true
                        },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (importance.value == Importance.LOW) MaterialTheme.colorScheme.tertiary
                    else if (importance.value == Importance.IMPORTANT) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onTertiary
                    }
                )
                MyDivider(modifier = Modifier.padding(vertical = 20.dp, horizontal = 26.dp))
                val formattedDate = remember {
                    derivedStateOf {
                        pickedDate.value?.let { formatDate(it) }
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
                        PrimaryBodyText(text = stringResource(R.string.do_before),
                            modifier = Modifier.clickable {
                                dateDialogState.show()
                            })
                        if (deadlineIsSelected.value && isChecked.value) {
                            Text(text = formattedDate.value ?: "",
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.clickable {
                                    dateDialogState.show()
                                })
                        }
                    }
                    Switch(
                        modifier = Modifier.padding(end = 40.dp),
                        checked = isChecked.value,
                        onCheckedChange = {
                            isChecked.value = it
                            if (isChecked.value) {
                                dateDialogState.show()
                            } else {
                                pickedDate.value = null
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
                        .let {
                            if (itemId.isNotBlank()) {
                                it.clickable {
                                    deleteTodoById(itemId)
                                    navController.popBackStack()
                                }
                            } else {
                                it
                            }
                        },
                    color = if (text.value.isEmpty()) MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.error
                )
                if (snackbarDateVisible.value) {
                    SnackBar(
                        text = stringResource(id = R.string.date_has_already_passed),
                        snackbarVisible = snackbarDateVisible
                    ) {
                        snackbarDateVisible.value = false
                    }
                }
                MaterialDialog(dialogState = dateDialogState, buttons = {
                    positiveButton(
                        text = stringResource(R.string.ready),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                    ) {
                        deadlineIsSelected.value = true
                        if (pickedDate.value?.isBefore(LocalDate.now()) == true) {
                            snackbarDateVisible.value = true
                        }
                    }
                    negativeButton(
                        text = stringResource(R.string.cancel),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                    ) {
                        isChecked.value = deadlineIsSelected.value
                    }
                }) {
                    datepicker(
                        initialDate = LocalDate.now(),
                        title = formatYear(LocalDate.now()),
                        colors = DatePickerDefaults.colors(
                            headerBackgroundColor = MaterialTheme.colorScheme.tertiary,
                            dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary,
                        )
                    ) {
                        pickedDate.value = it
                        isChecked.value = true
                    }
                }
            }
        }
    }
}

/**
 * Preview of the edit todo screen in light mode.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun EditTodoScreenPreviewLight() {
    ToDoApp3Theme(darkTheme = false) {
        val fakeUiState = EditTodoUiState(
            curTodoItem = TodoItem(
                id = "1",
                text = "Sample Todo",
                importance = Importance.LOW,
                deadline = LocalDate.now(),
                isCompleted = false,
                createdAt = LocalDate.now(),
                modifiedAt = LocalDate.now(),
                isSynced = false,
                isModified = false,
                isDeleted = false
            ),
            errorCode = null
        )

        val getTodoById: (String) -> Unit = {}
        val insertTodo: (TodoItem) -> Unit = {}
        val deleteTodoById: (String) -> Unit = {}
        val removeError: () -> Unit = {}
        val syncRemote: () -> Unit = {}

        val navController = rememberNavController()

        EditItemScreen(
            uiState = fakeUiState,
            getTodoById = getTodoById,
            insertTodo = insertTodo,
            deleteTodoById = deleteTodoById,
            removeError = removeError,
            syncRemote = syncRemote,
            navController = navController,
            itemId = "1"
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditTodoScreenPreviewDark() {
    ToDoApp3Theme(darkTheme = true) {
        val fakeUiState = EditTodoUiState(
            curTodoItem = TodoItem(
                id = "1",
                text = "text text text",
                importance = Importance.LOW,
                deadline = LocalDate.now(),
                isCompleted = false,
                createdAt = LocalDate.now(),
                modifiedAt = LocalDate.now(),
                isSynced = false,
                isModified = false,
                isDeleted = false
            ),
            errorCode = null
        )

        val getTodoById: (String) -> Unit = {}
        val insertTodo: (TodoItem) -> Unit = {}
        val deleteTodoById: (String) -> Unit = {}
        val removeError: () -> Unit = {}
        val syncRemote: () -> Unit = {}

        val navController = rememberNavController()

        EditItemScreen(
            uiState = fakeUiState,
            getTodoById = getTodoById,
            insertTodo = insertTodo,
            deleteTodoById = deleteTodoById,
            removeError = removeError,
            syncRemote = syncRemote,
            navController = navController,
            itemId = "1"
        )
    }
}
