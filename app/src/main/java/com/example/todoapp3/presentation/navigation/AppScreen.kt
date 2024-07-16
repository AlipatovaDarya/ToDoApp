package com.example.todoapp3.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.todoapp3.presentation.EditItemScreen
import com.example.todoapp3.presentation.TodoListScreen
import com.example.todoapp3.presentation.viewModel.EditItemViewModel
import com.example.todoapp3.presentation.viewModel.TodoListViewModel


/**
 * Composable function for provide NavHosts
 */
@Composable
fun AppScreen(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.APP_SCREEN,
    ) {
        navigation(
            route = MainDestinations.APP_SCREEN,
            startDestination = MainDestinations.HOME_LIST
        ) {


            composable(
                route = "${MainDestinations.ITEM_SCREEN}/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                val editTodoViewModel: EditItemViewModel = hiltViewModel()
                val uiStateEdit by editTodoViewModel.uiState.collectAsState()
                EditItemScreen(
                    uiState = uiStateEdit,
                    itemId = itemId,
                    navController = navController,
                    getTodoById = { id -> editTodoViewModel.getItemById(id) },
                    insertTodo = { item -> editTodoViewModel.insertItem(item) },
                    deleteTodoById = { id -> editTodoViewModel.deleteTodoById(id) },
                    removeError = { editTodoViewModel.removeError() },
                    syncRemote = { editTodoViewModel.syncRemote() }
                )
            }

            composable(MainDestinations.HOME_LIST) {
                val todoListViewModel: TodoListViewModel = hiltViewModel()
                val uiState by todoListViewModel.uiState.collectAsState()
                TodoListScreen(
                    uiState = uiState,
                    navController = navController,
                    onIsCompletedStatusChanged = { id ->
                        todoListViewModel.onIsCompletedStatusChanged(
                            id
                        )
                    },
                    removeError = { todoListViewModel.removeError() },
                    onInitializeConnectivityObserver = { context ->
                        todoListViewModel.initializeConnectivityObserver(
                            context
                        )
                    },
                    onVisibilityIsOnChange = { todoListViewModel.onVisibilityIsOnChange() },
                    syncRemote = { todoListViewModel.syncRemote() }
                )
            }


        }
    }
}

object MainDestinations {
    const val APP_SCREEN = "appScreen"
    const val HOME_LIST = "todoListScreen"
    const val ITEM_SCREEN = "editItemScreen"
}