package com.example.todoapp3

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoapp3.data.repository.ThemeRepositoryImpl
import com.example.todoapp3.presentation.navigation.AppScreen
import com.example.todoapp3.presentation.theme_settings.Themes
import com.example.todoapp3.presentation.viewModel.ThemeViewModel
import com.example.todoapp3.ui.theme.ToDoApp3Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeRepository: ThemeRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {

            val themeViewModel: ThemeViewModel = hiltViewModel()
            val theme = themeViewModel.theme.collectAsState()

            ToDoApp3Theme(
                darkTheme = when (theme.value) {
                    Themes.DARK -> true
                    Themes.LIGHT -> false
                    Themes.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController: NavHostController = rememberNavController()
                    AppScreen(navController = navController)
                }
            }
        }
    }
}

