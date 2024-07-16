package com.example.todoapp3.presentation

import android.content.Context
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.R
import com.example.todoapp3.data.network.ConnectivityObserver
import com.example.todoapp3.data.room.entity.TodoItem
import com.example.todoapp3.presentation.common.myShadow
import com.example.todoapp3.presentation.common.showErrorSnackbar
import com.example.todoapp3.presentation.model.Importance
import com.example.todoapp3.presentation.navigation.MainDestinations
import com.example.todoapp3.presentation.utils.getNetworkStatusMessage
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.google.accompanist.swiperefresh.SwipeRefreshState
import java.time.LocalDate


/**
 * Composable function for show home list of items
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    uiState: TodoListUiState,
    navController: NavController,
    onIsCompletedStatusChanged: (String) -> Unit,
    removeError: () -> Unit,
    onInitializeConnectivityObserver: (Context) -> Unit,
    onVisibilityIsOnChange: () -> Unit,
    syncRemote: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val swipeRefreshState = remember { SwipeRefreshState(false) }

    uiState.errorCode?.let { error ->
        LaunchedEffect(error) {
            showErrorSnackbar(context, error, snackbarHostState, scope, syncRemote)
            removeError()
        }
    }

    LaunchedEffect(Unit) {
        onInitializeConnectivityObserver(context)
    }

    LaunchedEffect(uiState.networkStatus) {
        val message = getNetworkStatusMessage(uiState.networkStatus, syncRemote)
        snackbarHostState.showSnackbar(message)
    }


    LaunchedEffect(swipeRefreshState.isRefreshing) {
        if (swipeRefreshState.isRefreshing) {
            val message = getNetworkStatusMessage(uiState.networkStatus, syncRemote)
            snackbarHostState.showSnackbar(message)
            swipeRefreshState.isRefreshing = false
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                modifier = Modifier
                    .shadow(
                        elevation = if (scrollBehavior.state.collapsedFraction > 0.5) 6.dp else 0.dp,
                        shape = RoundedCornerShape(0.dp)
                    ),
            ) {
                TopBar(
                    uiState = uiState,
                    modifier = Modifier
                        .padding(end = 34.dp)
                        .clickable { onVisibilityIsOnChange() },
                    scrollBehavior = scrollBehavior
                )

            }
        },
        floatingActionButton = {
            Fab(onClick = {
                navController.navigate("${MainDestinations.ITEM_SCREEN}/ ")
            })
        }

    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
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
                            .padding(start = 44.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${stringResource(R.string.done)} ${uiState.completedItemsCounter}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiary,
                        )
                        VisibilityImage(
                            uiState.visibilityIsOn,
                            modifier = Modifier
                                .padding(end = 34.dp)
                                .clickable { onVisibilityIsOnChange() }
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


            items(items = uiState.todoItems, key = { item -> item.id }) { item ->
                TodoItemScreen(todoItem = item, onCheckedChange = {
                    onIsCompletedStatusChanged(item.id)
                }) {
                    navController.navigate("${MainDestinations.ITEM_SCREEN}/${item.id}")
                }
            }

            item {
                BottomButtonNewTask {
                    navController.navigate("${MainDestinations.ITEM_SCREEN}/ ")
                }
            }
        }
    }

}


/**
 * Composable function for show topbar in home list of items
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    uiState: TodoListUiState,
    modifier: Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.my_items),
                style = if (scrollBehavior.state.collapsedFraction < 0.5)
                    MaterialTheme.typography.titleLarge
                else MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 26.dp)
            )
        },
        actions = {
            if (scrollBehavior.state.collapsedFraction > 0.8) {
                VisibilityImage(
                    uiState.visibilityIsOn,
                    modifier = modifier
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}


/**
 * Composable function for show VisibilityImage in home list of items
 */
@Composable
fun VisibilityImage(visibilityIsOn: Boolean, modifier: Modifier) {
    Image(
        painter = painterResource(
            if (visibilityIsOn) R.drawable.visibility_on
            else R.drawable.visibility_off
        ),
        contentDescription = if (visibilityIsOn) stringResource(R.string.hide_completed_tasks)
        else stringResource(R.string.show_completed_tasks),
        modifier = modifier,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
    )
}

/**
 * Composable function for show floating action button in home list of items
 */
@Composable
fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.tertiary,
        onClick = {
            onClick()
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

/**
 * Composable function for show bottom buttom "new" in home list of items
 */
@Composable
fun BottomButtonNewTask(onClick: () -> Unit) {
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
                onClick()
            },
        color = MaterialTheme.colorScheme.onTertiary,
        style = MaterialTheme.typography.bodyMedium
    )
}

/**
 * Preview of the Todo list screen in light mode.
 */
@Preview(showBackground = true, name = "Light Theme")
@Composable
fun TodoListPreviewLight() {
    val fakeTodoItems = listOf(
        TodoItem(
            id = "2",
            text = "Read a book",
            importance = Importance.LOW,
            deadline = null,
            isCompleted = false,
            createdAt = LocalDate.now(),
            modifiedAt = null,
            isSynced = false,
            isModified = false,
            isDeleted = false
        ),
        TodoItem(
            id = "3",
            text = "Read a book",
            importance = Importance.IMPORTANT,
            deadline = null,
            isCompleted = false,
            createdAt = LocalDate.now(),
            modifiedAt = null,
            isSynced = false,
            isModified = true,
            isDeleted = false
        ),
        TodoItem(
            id = "4",
            text = "Read a book",
            importance = Importance.BASIC,
            deadline = LocalDate.now(),
            isCompleted = false,
            createdAt = LocalDate.now(),
            modifiedAt = null,
            isSynced = false,
            isModified = false,
            isDeleted = false
        )
    )

    val fakeUiState = TodoListUiState(
        todoItems = fakeTodoItems,
        completedItemsCounter = 1,
        errorCode = null,
        visibilityIsOn = false,
        networkStatus = ConnectivityObserver.Status.Available
    )

    val navController = rememberNavController()

    ToDoApp3Theme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            TodoListScreen(
                uiState = fakeUiState,
                navController = navController,
                onVisibilityIsOnChange = {},
                removeError = {},
                onInitializeConnectivityObserver = {},
                onIsCompletedStatusChanged = {},
                syncRemote = {}
            )
        }
    }
}


/**
 * Preview of the Todo List screen in dark mode.
 */
@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun TodoListScreenPreviewDark() {
    val fakeTodoItems = listOf(
        TodoItem(
            id = "2",
            text = "Read a book",
            importance = Importance.LOW,
            deadline = null,
            isCompleted = false,
            createdAt = LocalDate.now(),
            modifiedAt = null,
            isSynced = false,
            isModified = false,
            isDeleted = false
        ),
        TodoItem(
            id = "3",
            text = "Read a book",
            importance = Importance.IMPORTANT,
            deadline = null,
            isCompleted = false,
            createdAt = LocalDate.now(),
            modifiedAt = null,
            isSynced = false,
            isModified = true,
            isDeleted = false
        )
    )

    val fakeUiState = TodoListUiState(
        todoItems = fakeTodoItems,
        completedItemsCounter = 1,
        errorCode = null,
        visibilityIsOn = false,
        networkStatus = ConnectivityObserver.Status.Available
    )

    val navController = rememberNavController()

    ToDoApp3Theme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            TodoListScreen(
                uiState = fakeUiState,
                navController = navController,
                onVisibilityIsOnChange = {},
                removeError = {},
                onInitializeConnectivityObserver = {},
                onIsCompletedStatusChanged = {},
                syncRemote = {}
            )
        }
    }
}