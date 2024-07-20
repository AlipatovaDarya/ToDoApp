package com.example.todoapp3.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.todoapp3.presentation.about_app.AboutAppScreen
import com.example.todoapp3.presentation.edit_task.EditItemScreen
import com.example.todoapp3.presentation.theme_settings.ThemeSettingsScreen
import com.example.todoapp3.presentation.task_list.TodoListScreen
import com.example.todoapp3.presentation.viewModel.EditItemViewModel
import com.example.todoapp3.presentation.viewModel.ThemeViewModel
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
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation(
            route = MainDestinations.APP_SCREEN,
            startDestination = MainDestinations.HOME_LIST
        ) {

            composable(
                route = "${MainDestinations.ITEM_SCREEN}/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
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

            composable(
                route = MainDestinations.SETTINGS_SCREEN,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
                ) {
                val themeViewModel: ThemeViewModel = hiltViewModel()
                ThemeSettingsScreen(
                    navController = navController,
                    themeViewModel
                )
            }
            composable(
                route = MainDestinations.ABOUT_APP_SCREEN,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                val themeViewModel: ThemeViewModel = hiltViewModel()
                val theme by themeViewModel.theme.collectAsState()
                AboutAppScreen(
                    navController = navController,
                    theme = theme
                )
            }


        }
    }
}

object MainDestinations {
    const val APP_SCREEN = "appScreen"
    const val HOME_LIST = "todoListScreen"
    const val ITEM_SCREEN = "editItemScreen"
    const val SETTINGS_SCREEN = "settingsScreen"
    const val ABOUT_APP_SCREEN = "aboutAppScreen"
}