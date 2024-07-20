package com.example.todoapp3.presentation.edit_task

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.R
import com.example.todoapp3.data.room.entity.TodoItem
import com.example.todoapp3.presentation.common.MyDivider
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val importantIsSelected = remember { mutableStateOf(false) }
    val deadlineIsSelected = remember { mutableStateOf(uiState.curTodoItem?.deadline != null) }
    val isChecked = remember { mutableStateOf(uiState.curTodoItem?.deadline != null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarDateVisible = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }

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
                        val mutableInteractionSource = remember { MutableInteractionSource() }
                        Image(
                            painter = painterResource(R.drawable.close),
                            contentDescription = stringResource(R.string.close_task),
                            modifier = Modifier
                                .padding(start = 26.dp, top = 6.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = mutableInteractionSource
                                ) {
                                    navController.popBackStack()
                                },
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                        )
                    },
                    title = {},
                    actions = {
                        SaveButton {
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
                        }
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
            val mutableInteractionSource = remember { MutableInteractionSource() }
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
                Text(
                    text = stringResource(R.string.importance),
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = mutableInteractionSource
                    ) {
                        showBottomSheet.value = true
                    },
                    style = MaterialTheme.typography.bodyMedium,

                    )


                if (showBottomSheet.value) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet.value = false
                            importantIsSelected.value = !importantIsSelected.value
                            scope.launch {
                                delay(200.toLong())
                                importantIsSelected.value = !importantIsSelected.value
                            }
                        },
                        sheetState = sheetState,
                    ) {

                        Column(Modifier.selectableGroup())
                        {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (importance.value == Importance.IMPORTANT),
                                    onClick = {
                                        importance.value = Importance.IMPORTANT
                                    }
                                )
                                Text(
                                    text = Importance.IMPORTANT.text
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (importance.value == Importance.BASIC),
                                    onClick = { importance.value = Importance.BASIC }
                                )
                                Text(
                                    text = Importance.BASIC.text
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (importance.value == Importance.LOW),
                                    onClick = { importance.value = Importance.LOW }
                                )
                                Text(
                                    text = Importance.LOW.text
                                )
                            }
                        }
                        Spacer(modifier = Modifier.padding(26.dp))
                    }
                }
            }

            val importantColor by animateColorAsState(
                targetValue = if (!importantIsSelected.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onTertiaryContainer,
                animationSpec = tween(200), label = ""
            )

            Text(
                text = if (importance.value == Importance.IMPORTANT) "!!${importance.value.text}"
                else importance.value.text,
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 2.dp)
                    .clickable(
                        indication = null,
                        interactionSource = mutableInteractionSource
                    ) {
                        showBottomSheet.value = true
                    },
                style = MaterialTheme.typography.bodySmall,
                color = if (importance.value == Importance.LOW) MaterialTheme.colorScheme.primary
                else
                    if (importance.value == Importance.IMPORTANT) {
                        importantColor
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
                    Text(
                        text = stringResource(R.string.do_before),
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = mutableInteractionSource
                        ) {
                            dateDialogState.show()
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (deadlineIsSelected.value && isChecked.value) {
                        Text(text = formattedDate.value ?: "",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = mutableInteractionSource
                            ) {
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
                        uncheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                        checkedIconColor = MaterialTheme.colorScheme.primary,
                        disabledCheckedIconColor = MaterialTheme.colorScheme.primary,
                        disabledCheckedThumbColor = MaterialTheme.colorScheme.outlineVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.outline,
                    )
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            MyDivider(modifier = Modifier.padding(vertical = 10.dp, horizontal = 26.dp))

            DeleteButton(itemId = itemId) {
                deleteTodoById(itemId)
                navController.popBackStack()
            }

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
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    deadlineIsSelected.value = true
                    if (pickedDate.value?.isBefore(LocalDate.now()) == true) {
                        snackbarDateVisible.value = true
                    }
                }
                negativeButton(
                    text = stringResource(R.string.cancel),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    isChecked.value = deadlineIsSelected.value
                }
            }) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = formatYear(LocalDate.now()),
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = MaterialTheme.colorScheme.primary,
                        dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    pickedDate.value = it
                    isChecked.value = true
                }
            }
        }
    }

}

@Composable
fun SaveButton(onClick: () -> Unit) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val pressed = mutableInteractionSource.collectIsPressedAsState()

    val scale = animateFloatAsState(
        targetValue = if (pressed.value) 0.6f else 1f,
        animationSpec = tween(1000),
        label = "scale"
    )

    Text(
        text = stringResource(R.string.save),
        modifier = Modifier
            .padding(end = 26.dp, top = 6.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                transformOrigin = TransformOrigin.Center
            }
            .clickable(
                indication = null,
                interactionSource = mutableInteractionSource
            ) {
                onClick()
            },
        color = MaterialTheme.colorScheme.primary,
    )
}


@SuppressLint("RememberReturnType")
@Composable
fun DeleteButton(itemId: String, onClick: () -> Unit) {

    val mutableInteractionSource = remember { MutableInteractionSource() }
    val pressed = mutableInteractionSource.collectIsPressedAsState()


    val scale = animateFloatAsState(
        targetValue = if (pressed.value) 0.8f else 1f,
        animationSpec = tween(600),
        label = "scale"
    )

    Text(
        text = stringResource(R.string.delete_task),
        modifier = Modifier
            .padding(top = 20.dp, start = 60.dp, bottom = 70.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                transformOrigin = TransformOrigin.Center
            }
            .let {
                if (itemId.isNotBlank()) {
                    it.clickable(
                        interactionSource = mutableInteractionSource,
                        indication = null
                    ) {
                        onClick()
                    }
                } else {
                    it
                }
            },
        style = LocalTextStyle.current.copy(textMotion = TextMotion.Animated),
        color = if (!pressed.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onTertiaryContainer,
    )
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
