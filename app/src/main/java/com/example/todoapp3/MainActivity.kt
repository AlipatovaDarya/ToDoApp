package com.example.todoapp3

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.navigation.AppScreen
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import com.example.todoapp3.viewModels.TodoItemsViewModel
import com.example.todoapp3.viewModels.TodoViewModelFactory

class MainActivity : ComponentActivity() {

    private val todoItemsViewModel: TodoItemsViewModel by viewModels {
        TodoViewModelFactory((application as ToDoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoApp3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController: NavHostController = rememberNavController()
                    AppScreen(todoItemsViewModel = todoItemsViewModel, navController = navController)
                }
            }
        }
    }
}

