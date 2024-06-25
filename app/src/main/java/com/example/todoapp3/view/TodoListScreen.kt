package com.example.todoapp3.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todoapp3.R
import com.example.todoapp3.model.TodoItem
import com.example.todoapp3.navigation.MainDestinations
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.example.todoapp3.view.general.myShadow
import com.example.todoapp3.viewModels.TodoItemsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    todoItemsViewModel: TodoItemsViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    ToDoApp3Theme {
        val completedItemsVisibilityIsOn = todoItemsViewModel.visibilityIsOn.observeAsState()
        val curItemsList: State<MutableList<TodoItem>>
        if(completedItemsVisibilityIsOn.value == true){
            curItemsList = todoItemsViewModel.todoItems.observeAsState(mutableListOf<TodoItem>())
        } else{
            curItemsList = todoItemsViewModel.uncompletedItems.observeAsState(mutableListOf<TodoItem>())
        }
        val completedItemsCounter = todoItemsViewModel.completedItemsCounter.observeAsState()
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = if (scrollBehavior.state.collapsedFraction > 0.5) 6.dp else 0.dp,
                            shape = RoundedCornerShape(0.dp)
                        )
                ) {
                    LargeTopAppBar(
                        title = {
                            Column {
                                if (scrollBehavior.state.collapsedFraction < 0.5) {
                                    Text(text =
                                    stringResource(R.string.my_items),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(start = 26.dp)
                                    )
                                } else {
                                    Text(
                                        stringResource(R.string.my_items),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(start = 26.dp)
                                    )
                                }

                            }
                        },
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,

                            ),
                        actions = {
                            if (scrollBehavior.state.collapsedFraction > 0.8) {
                                Image(
                                    painter = painterResource(
                                        if (completedItemsVisibilityIsOn.value == true) R.drawable.visibility_on
                                        else R.drawable.visibility_off
                                    ),
                                    contentDescription = if (completedItemsVisibilityIsOn.value == true) stringResource(R.string.hide_completed_tasks)
                                    else stringResource(R.string.show_completed_tasks),
                                    modifier = Modifier
                                        .clickable {
                                            todoItemsViewModel.visibilityIsOn.value =
                                                todoItemsViewModel.visibilityIsOn.value?.let { !it }
                                                    ?: false
                                        }
                                        .padding(end = 26.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    onClick = {
                        todoItemsViewModel.curItem.value = null
                        navController.navigate(MainDestinations.ITEM_SCREEN)
                    },
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 18.dp, end = 18.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.add),
                        contentDescription = stringResource(R.string.add_new_task),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    ),
            )
            {
                item {
                    AnimatedVisibility(
                        visible = scrollBehavior.state.collapsedFraction < 0.5,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 44.dp, end = 34.dp, bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "${stringResource(R.string.done)} ${completedItemsCounter.value}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiary,
                            )
                            Image(
                                painter = painterResource(
                                    if (completedItemsVisibilityIsOn.value == true) R.drawable.visibility_on
                                    else R.drawable.visibility_off
                                ),
                                contentDescription = if (completedItemsVisibilityIsOn.value == true) stringResource(R.string.hide_completed_tasks)
                                else stringResource(R.string.show_completed_tasks),
                                modifier = Modifier
                                    .clickable {
                                        todoItemsViewModel.visibilityIsOn.value = todoItemsViewModel.visibilityIsOn.value?.let { !it } ?: false
                                    },
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                            )
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        )
                    }
                }
                items(curItemsList.value){ item ->
                    TodoItemScreen(todoItem = item, todoItemsViewModel = todoItemsViewModel, navController = navController)
                }

                item {
                    Text(
                        text = stringResource(R.string.new_),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 30.dp)
                            .myShadow(offsetY = 2.5.dp, blurRadius = 2.5.dp, borderRadius = 10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomStart = 12.dp,
                                    bottomEnd = 12.dp
                                )
                            )
                            .padding(bottom = 20.dp, start = 48.dp, top = 18.dp)
                            .clickable {
                                todoItemsViewModel.curItem.value = null
                                navController.navigate(MainDestinations.ITEM_SCREEN)
                            },
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}