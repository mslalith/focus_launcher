package dev.mslalith.focuslauncher.core.ui.controller

import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

interface SystemUiController {
    fun showStatusBars()
    fun hideStatusBars()

    fun setStatusBarColor(
        color: Color,
        darkIcons: Boolean = color.luminance() > 0.5f
    )

    fun setNavigationBarColor(
        color: Color,
        darkIcons: Boolean = color.luminance() > 0.5f
    )
}

fun SystemUiController.toggleStatusBars(show: Boolean) = if (show) showStatusBars() else hideStatusBars()

fun SystemUiController.setSystemBarsColor(color: Color) {
    setStatusBarColor(color = color)
    setNavigationBarColor(color = color)
}

class SystemUiControllerImpl(
    private val window: Window,
    view: View
) : SystemUiController {

    private val windowInsetsController = WindowCompat.getInsetsController(window, view)

    private var statusBarDarkContentEnabled: Boolean
        get() = windowInsetsController.isAppearanceLightStatusBars
        set(value) { windowInsetsController.isAppearanceLightStatusBars = value }

    private var navigationBarDarkContentEnabled: Boolean
        get() = windowInsetsController.isAppearanceLightNavigationBars
        set(value) { windowInsetsController.isAppearanceLightNavigationBars = value }

    private var isNavigationBarContrastEnforced: Boolean
        get() = Build.VERSION.SDK_INT >= 29 && window.isNavigationBarContrastEnforced
        set(value) {
            if (Build.VERSION.SDK_INT >= 29) window.isNavigationBarContrastEnforced = value
        }


    override fun showStatusBars() = windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    override fun hideStatusBars() = windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

    override fun setStatusBarColor(color: Color, darkIcons: Boolean) {
        statusBarDarkContentEnabled = darkIcons

        window.statusBarColor = when {
            darkIcons && !statusBarDarkContentEnabled -> {
                // If we're set to use dark icons, but our windowInsetsController call didn't
                // succeed (usually due to API level), we instead transform the color to maintain
                // contrast
                BlackScrimmed(color)
            }
            else -> color
        }.toArgb()
    }

    override fun setNavigationBarColor(
        color: Color,
        darkIcons: Boolean
    ) {
        navigationBarDarkContentEnabled = darkIcons
        isNavigationBarContrastEnforced = true

        window.navigationBarColor = when {
            darkIcons && !navigationBarDarkContentEnabled -> {
                // If we're set to use dark icons, but our windowInsetsController call didn't
                // succeed (usually due to API level), we instead transform the color to maintain
                // contrast
                BlackScrimmed(color)
            }
            else -> color
        }.toArgb()
    }
}

private val BlackScrim = Color(0f, 0f, 0f, 0.3f) // 30% opaque black
private val BlackScrimmed: (Color) -> Color = { original ->
    BlackScrim.compositeOver(original)
}
