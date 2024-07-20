package com.example.todoapp3.presentation.theme_settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material3.ripple
import com.example.todoapp3.R
import com.example.todoapp3.presentation.viewModel.ThemeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    navController: NavController,
    viewModel: ThemeViewModel
) {
    val selectedTheme = viewModel.theme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.arrow_back_text),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = ripple()
                            ) { navController.popBackStack() },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.choose_theme),
                        style = MaterialTheme.typography.titleMedium
                    )
                },

                )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Themes.values().forEachIndexed { index, theme ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            when (index) {
                                0 -> {
                                   viewModel.setTheme(Themes.LIGHT)
                                }

                                1 -> {
                                    viewModel.setTheme(Themes.DARK)
                                }
                                else -> {
                                    viewModel.setTheme(Themes.SYSTEM)
                                }
                            }
                        }
                ) {
                    RadioButton(
                        selected = selectedTheme.value.number == index,
                        onClick = {
                            when (index) {
                                0 -> {
                                    viewModel.setTheme(Themes.LIGHT)
                                }
                                1 -> {
                                    viewModel.setTheme(Themes.DARK)
                                }
                                else -> {
                                    viewModel.setTheme(Themes.SYSTEM)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = theme.text,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}


enum class Themes {
    LIGHT {
        override val text = "Светлая"
        override val number = 0
    },
    DARK {
        override val text = "Тёмная"
        override val number = 1
    },
    SYSTEM {
        override val text = "Системная"
        override val number = 2
    };

    abstract val text: String
    abstract val number: Int
}
