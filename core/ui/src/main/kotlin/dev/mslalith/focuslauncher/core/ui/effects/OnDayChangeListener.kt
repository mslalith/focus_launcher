package dev.mslalith.focuslauncher.core.ui.effects

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.collections.immutable.persistentListOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OnDayChangeListener(
    onDayChange: () -> Unit
) {
    var lastDate = rememberSaveable { Date() }
    val dateFormat = remember { SimpleDateFormat("yyMMdd", Locale.getDefault()) }

    val updatedOnDayChange by rememberUpdatedState(newValue = onDayChange)

    fun isSameDay(currentDate: Date) = dateFormat.format(currentDate) == dateFormat.format(lastDate)

    SystemBroadcastReceiver(
        systemActions = persistentListOf(
            Intent.ACTION_TIME_TICK,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED
        ),
        onSystemEvent = {
            val currentDate = Date()
            if (!isSameDay(currentDate = currentDate)) {
                updatedOnDayChange()
                lastDate = currentDate
            }
        }
    )
}
