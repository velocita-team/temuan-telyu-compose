package id.my.ariqnf.temuantelyu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Red600,
    onPrimary = Color.White,
    secondary = Red500,
    onSecondary = Color.White,
    tertiary = Green600,
    onTertiary = Color.White,
    background = Gray900,
    surface = Gray800,
    onSurface = Gray50,
    surfaceVariant = Gray50.copy(0.12f)
)

private val LightColorScheme = lightColorScheme(
    primary = Red600,
    onPrimary = Color.White,
    secondary = Red500,
    onSecondary = Color.White,
    tertiary = Green600,
    onTertiary = Color.White,
    background = Color.White,
    surface = Gray50,
    onSurface = Color.Black,
    surfaceVariant = Color.Black.copy(0.12f)
)

@Composable
fun TemuanTelyuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}