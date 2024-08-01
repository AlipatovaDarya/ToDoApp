package com.example.todoapp3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp3.data.repository.ThemeRepositoryImpl
import com.example.todoapp3.presentation.theme_settings.Themes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepositoryImpl
) : ViewModel() {

    private val _theme = MutableStateFlow(Themes.SYSTEM)
    val theme: StateFlow<Themes> = _theme

    init {
        /*viewModelScope.launch {
            themeRepository.themeFlow
                .onEach { theme ->
                    _theme.update { theme }
                }
        }*/
        viewModelScope.launch {
            themeRepository.themeFlow.collect{ theme ->
                _theme.update { theme }
            }
        }
    }

    fun setTheme(theme: Themes) {
        viewModelScope.launch {
            themeRepository.setTheme(theme)
        }
    }
}