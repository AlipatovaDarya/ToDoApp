package com.example.todoapp3.data.repository

import com.example.todoapp3.presentation.theme_settings.Themes
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun setTheme(theme: Themes)
    val themeFlow: Flow<Themes>
}
