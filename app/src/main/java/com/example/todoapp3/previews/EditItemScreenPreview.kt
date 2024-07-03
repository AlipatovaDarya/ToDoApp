package com.example.todoapp3.previews


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.repositories.TodoItemsRepository
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.example.todoapp3.view.edit_item.EditItemScreen
import com.example.todoapp3.viewModels.TodoItemsViewModel



@Preview(showBackground = true)
@Composable
fun EditItemScreenPreview() {
    ToDoApp3Theme {
        val repository = TodoItemsRepository()
        val todoItemsViewModel = TodoItemsViewModel(repository)
        val navController = rememberNavController()
        EditItemScreen(todoItemsViewModel = todoItemsViewModel, navController = navController)

    }
}