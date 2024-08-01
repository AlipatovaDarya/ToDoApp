package com.example.todoapp3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    tertiary = ThemeColors.Night.supportSeparator,
    secondary = ThemeColors.Night.supportOverlay,
    primary = ThemeColors.Night.blue,
    error = ThemeColors.Night.red,
    scrim = ThemeColors.Night.labelSecondary,
    surfaceContainerLowest = ThemeColors.Night.green,
    outlineVariant = ThemeColors.Night.gray,
    outline = ThemeColors.Night.grayLight,

    surface = ThemeColors.Night.backPrimary,
    background = ThemeColors.Night.backSecondary,
    surfaceTint = ThemeColors.Night.backElevated,


    onPrimary = ThemeColors.Night.labelPrimary,
    onSecondary = ThemeColors.Night.labelSecondary ,
    onTertiary = ThemeColors.Night.labelTertiary,
    onSurfaceVariant = ThemeColors.Night.labelDisable,
    onTertiaryContainer = ThemeColors.Night.redLight
)

private val LightColorScheme = lightColorScheme(
    tertiary= ThemeColors.Day.supportSeparator,
    secondary = ThemeColors.Day.supportOverlay,
    primary = ThemeColors.Day.blue,
    error = ThemeColors.Day.red,
    scrim = ThemeColors.Day.labelSecondary,
    surfaceContainerLowest = ThemeColors.Day.green,
    outlineVariant = ThemeColors.Day.gray,
    outline = ThemeColors.Day.grayLight,


    surface = ThemeColors.Day.backPrimary,
    background = ThemeColors.Day.backSecondary,
    surfaceTint = ThemeColors.Day.backElevated,

    onPrimary = ThemeColors.Day.labelPrimary,
    onSecondary = ThemeColors.Day.labelSecondary ,
    onTertiary = ThemeColors.Day.labelTertiary,
    onSurfaceVariant = ThemeColors.Day.labelDisable,
    onTertiaryContainer = ThemeColors.Day.redLight
)

@Composable
fun ToDoApp3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun LightColorPalettePreview() {
    ToDoApp3Theme {
        Column {
            Box(modifier = Modifier
                .background(ThemeColors.Day.white)
                .padding(4.dp)){
                Column {
                    Text(text = "Large Tittle - 32/38", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Tittle - 20/32", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Button - 14/24", style = MaterialTheme.typography.titleSmall)
                    Text(text = "Body - 16/20", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Subhead - 14/20", style = MaterialTheme.typography.bodySmall)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.supportSeparator)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Support [Light] / Separator\n#0x33000000",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.supportOverlay)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Support [Light] / Overlay\n#0x0F000000",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
                Spacer(Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row{
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.labelSecondary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Label [Light] / Primary\n#0x99000000",
                        textColor = ThemeColors.Day.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.labelPrimary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Label [Light] / Secondary\n#0x99000000",
                        textColor = ThemeColors.Day.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.labelTertiary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Label [Light] / Tertiary\n#0x4D000000",
                        textColor = ThemeColors.Day.white,
                    )
                }
            }
            Box(modifier = Modifier
                .background(color = ThemeColors.Day.labelDisable)
                .fillMaxWidth(0.33f)
                .height(60.dp)
            ){
                smallPreviewText(
                    text = "Label [Light] / Disable\n#0x26000000",
                    textColor = ThemeColors.Day.labelPrimary,
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row {
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.red)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Light] / Red    \n#0xFFFF3B30",
                        textColor = ThemeColors.Day.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.green)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Light] / Green  \n#0xFF34C759",
                        textColor = ThemeColors.Day.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.blue)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Light] / Blue   \n#0xFF007AFF",
                        textColor = ThemeColors.Day.white,
                    )
                }
            }
            Row {
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.gray)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Light] / Gray\n#0xFF8E8E93",
                        textColor = ThemeColors.Day.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.grayLight)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Light] / Gray Light\n#0xFFD1D1D6",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.white)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Light] / White\n#0xFFFFFFFF",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row{
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.backPrimary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Back [Light] / Primary\n#0xFFF7F6F2",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.backSecondary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Back [Light] / Secondary\n#0xFFFFFFFF",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Day.backElevated)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Back [Light] / Elevated\n#0xFFFFFFFF",
                        textColor = ThemeColors.Day.labelPrimary,
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DarkColorPalettePreview() {
    ToDoApp3Theme {
        Column(
            modifier = Modifier
                .background(Color(0xDD000000))
        ) {
            Box(modifier = Modifier
                .padding(4.dp)){
                Column {
                    Text(text = "Large Tittle - 32/38", style = MaterialTheme.typography.titleLarge, color = ThemeColors.Day.white)
                    Text(text = "Tittle - 20/32", style = MaterialTheme.typography.titleMedium, color = ThemeColors.Day.white)
                    Text(text = "Button - 14/24", style = MaterialTheme.typography.titleSmall, color = ThemeColors.Day.white)
                    Text(text = "Body - 16/20", style = MaterialTheme.typography.bodyMedium, color = ThemeColors.Day.white)
                    Text(text = "Subhead - 14/20", style = MaterialTheme.typography.bodySmall, color = ThemeColors.Day.white)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.supportSeparator)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Support [Dark] / Separator\n#0x33000000",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.supportOverlay)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Support [Dark] / Overlay\n#0x0F000000",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
                Spacer(Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row{
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.labelSecondary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Label [Dark] / Primary\n#0x99000000",
                        textColor = ThemeColors.Night.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.labelPrimary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Label [Dark] / Secondary\n#0x99000000",
                        textColor = ThemeColors.Night.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.labelTertiary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Label [Dark] / Tertiary\n#0x4D000000",
                        textColor = ThemeColors.Night.white,
                    )
                }
            }
            Box(modifier = Modifier
                .background(color = ThemeColors.Night.labelDisable)
                .fillMaxWidth(0.33f)
                .height(60.dp)
            ){
                smallPreviewText(
                    text = "Label [Dark] / Disable\n#0x26000000",
                    textColor = ThemeColors.Night.labelPrimary,
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row {
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.red)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Dark] / Red    \n#0xFFFF3B30",
                        textColor = ThemeColors.Night.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.green)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Dark] / Green  \n#0xFF34C759",
                        textColor = ThemeColors.Night.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.blue)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Dark] / Blue   \n#0xFF007AFF",
                        textColor = ThemeColors.Night.white,
                    )
                }
            }
            Row {
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.gray)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Dark] / Gray\n#0xFF8E8E93",
                        textColor = ThemeColors.Night.white,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.grayLight)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Dark] / Gray Light\n#0xFFD1D1D6",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.white)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Color [Dark] / White\n#0xFFFFFFFF",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            Row{
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.backPrimary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Back [Dark] / Primary\n#0xFFF7F6F2",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.backSecondary)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Back [Dark] / Secondary\n#0xFFFFFFFF",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
                Box(modifier = Modifier
                    .background(color = ThemeColors.Night.backElevated)
                    .weight(1f)
                    .height(60.dp)
                ){
                    smallPreviewText(
                        text = "Back [Dark] / Elevated\n#0xFFFFFFFF",
                        textColor = ThemeColors.Night.labelPrimary,
                    )
                }
            }
        }

    }
}

@Composable
fun smallPreviewText(text: String, textColor: Color){
    Text(
        text = text,
        style = TextStyle(color = textColor, fontSize = 10.sp),
        modifier = Modifier
            .padding(start = 4.dp, end = 2.dp, top = 26.dp, bottom = 4.dp)
    )
}