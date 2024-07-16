package com.example.todoapp3

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.presentation.navigation.AppScreen
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            ToDoApp3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController: NavHostController = rememberNavController()
                    AppScreen(navController = navController)
                }
            }
        }
    }
}

