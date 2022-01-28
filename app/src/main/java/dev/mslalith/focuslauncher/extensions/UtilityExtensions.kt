package dev.mslalith.focuslauncher.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import dev.mslalith.focuslauncher.data.database.entities.App
import dev.mslalith.focuslauncher.data.models.AppWithIcon
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Month
import java.time.ZonedDateTime
import java.util.Locale

fun runAfter(delayMillis: Long, action: () -> Unit) = Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)

fun waitAndRunAfter(
    startTime: Long,
    minTime: Long = 2000L,
    action: () -> Unit,
) {
    val elapsedTime = System.currentTimeMillis() - startTime
    val waitTimeMillis = if (elapsedTime < minTime) minTime - elapsedTime else 0
    runAfter(
        delayMillis = waitTimeMillis,
        action = action,
    )
}

fun Instant.formatToTime(): String {
    return toLocalDateTime(TimeZone.currentSystemDefault()).run {
        listOf(hour, minute).map { it.to2Digit() }.joinToString(separator = ":")
    }
}

fun ZonedDateTime.isSameAsToday(): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val (year, month, day) = this.toString().substringBefore("T").split("-")
    return today.year == year.toInt() && today.monthNumber == month.toInt() && today.dayOfMonth == day.toInt()
}

fun ZonedDateTime.inShortReadableFormat(): String {
    val (_, month, day) = this.toString().substringBefore("T").split("-")
    val monthReadable = Month.values()[month.toInt() - 1].toString()
        .lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val daySuffix = when (day.toInt()) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    return "$day$daySuffix $monthReadable"
}

fun LocalDateTime.inShortReadableFormat(): String {
    val daySuffix = when (dayOfMonth) {
        1, 21, 31 -> "st"
        2, 22 -> "nd"
        3, 23 -> "rd"
        else -> "th"
    }
    val monthReadable =
        month.toString()
            .lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    return "$dayOfMonth$daySuffix $monthReadable"
}

fun Double.asPercent(precision: Int = 2) = "%.${precision}f".format(this) + "%"

fun Char.isAlphabet() = when (code) {
    in 65..90 -> true
    in 97..122 -> true
    else -> false
}

private fun Int.to2Digit() = when {
    this < 10 -> "0$this"
    else -> this
}

fun App.toAppWithIcon(context: Context): AppWithIcon? = context.iconOf(packageName)?.let { icon ->
    AppWithIcon(
        name = name,
        packageName = packageName,
        icon = icon,
        isSystem = isSystem
    )
}

fun List<App>.toAppWithIconList(context: Context) =
    mapNotNull {
        try {
            it.toAppWithIcon(context)
        } catch (ex: PackageManager.NameNotFoundException) {
            null
        }
    }
