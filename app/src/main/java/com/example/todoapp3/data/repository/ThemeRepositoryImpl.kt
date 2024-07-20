package com.example.todoapp3.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.todoapp3.presentation.theme_settings.Themes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(private val dataStore: DataStore<Preferences>) : ThemeRepository {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_key")
    }

    override val themeFlow: Flow<Themes> = dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY] ?: Themes.SYSTEM.name) {
                Themes.LIGHT.name -> Themes.LIGHT
                Themes.DARK.name -> Themes.DARK
                else -> Themes.SYSTEM
            }
        }

    override suspend fun setTheme(theme: Themes) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

}