package dev.mslalith.focuslauncher.core.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
sealed interface UiText {

    data class Static(val text: String) : UiText
    data class Resource(@StringRes val stringRes: Int) : UiText

    fun string(context: Context): String = when (this) {
        is Static -> text
        is Resource -> context.getString(stringRes)
    }
}
