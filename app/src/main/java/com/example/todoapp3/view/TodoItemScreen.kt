package com.example.todoapp3.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoapp3.R
import com.example.todoapp3.model.Importance
import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.navigation.MainDestinations
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.example.todoapp3.view.general.myShadow
import com.example.todoapp3.viewModels.TodoItemsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun TodoItemScreen(
    todoItem: TodoItem,
    todoItemsViewModel: TodoItemsViewModel,
    navController: NavController
) {
    ToDoApp3Theme {
        val checkedState = remember { mutableStateOf(todoItem.isCompleted) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .myShadow(offsetY = 3.dp, blurRadius = 2.5.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 4.dp, end = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Checkbox(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    todoItem.isCompleted = it
                    todoItemsViewModel.getItemById(todoItem.id)
                    todoItemsViewModel.changeIsCompletedStatus(
                        item = todoItem,
                        isCompleted = checkedState.value
                    )
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.scrim,
                    uncheckedColor =
                    if (todoItem.importance == Importance.HIGH) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onTertiary,
                    checkmarkColor = MaterialTheme.colorScheme.surface,
                    disabledCheckedColor = MaterialTheme.colorScheme.scrim,
                    disabledUncheckedColor = if (todoItem.importance == Importance.HIGH) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onTertiary,
                ),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp)
            ) {
                Box {
                    var itemText = todoItem.text
                    if (!checkedState.value && todoItem.importance == Importance.HIGH) {
                        Image(
                            painter = painterResource(R.drawable.exclamation_marks),
                            contentDescription = stringResource(R.string.task_has_status_high),
                            modifier = Modifier
                                .padding(top = 4.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
                        )
                        itemText = "    $itemText"
                    } else if (!checkedState.value && todoItem.importance == Importance.LOW) {
                        Image(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.task_has_status_low),
                            modifier = Modifier
                                .rotate(270f)
                                .padding(top = 4.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
                        )
                        itemText = "     $itemText"
                    }
                    Text(
                        text = itemText,
                        modifier = Modifier
                            .padding(top = 2.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = if (checkedState.value) TextDecoration.LineThrough
                        else TextDecoration.None,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (checkedState.value) MaterialTheme.colorScheme.onTertiary
                        else MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                todoItem.deadline?.let {
                    Text(
                        text = it.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.info),
                contentDescription = stringResource(R.string.show_information),
                modifier = Modifier
                    .clickable {
                        todoItemsViewModel.onCurItemOnChange(todoItem)
                        navController.navigate(MainDestinations.ITEM_SCREEN)
                    }
                    .padding(start = 8.dp, end = 10.dp, top = 12.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
            )
        }

    }
}