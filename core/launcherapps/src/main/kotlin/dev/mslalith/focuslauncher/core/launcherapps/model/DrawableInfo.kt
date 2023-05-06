package dev.mslalith.focuslauncher.core.launcherapps.model

import androidx.annotation.DrawableRes
import java.util.Calendar

internal sealed class DrawableInfo(
    open val drawableName: String
) {
    @DrawableRes
    abstract fun getDrawableResId(): Int
}

internal class SimpleDrawableInfo(
    override val drawableName: String,
    @DrawableRes val drawableId: Int
) : DrawableInfo(drawableName = drawableName) {
    override fun getDrawableResId(): Int = drawableId
}

internal class CalendarDrawableInfo(
    override val drawableName: String
) : DrawableInfo(drawableName = drawableName) {

    private val drawableForDay = IntArray(size = 31)

    fun setDrawableForDay(day: Int, @DrawableRes drawableId: Int) {
        drawableForDay[day] = drawableId
    }

    override fun getDrawableResId(): Int = drawableForDay[Calendar.getInstance()[Calendar.DAY_OF_MONTH] - 1]
}
