package karldrabek.goobaapp.goobaapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryPurple,
    background = AppBackground,
    surface = CardBackground,
    surfaceVariant = InputBackground,
    onPrimary = ButtonTextOnDark,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = MutedText,
    outline = CardBorder
)

private val DarkColors = darkColorScheme(
    primary = PrimaryPurple,
    background = AppBackground,
    surface = CardBackground,
    surfaceVariant = InputBackground,
    onPrimary = ButtonTextOnDark,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = MutedText,
    outline = CardBorder
)

@Composable
fun GoobaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}