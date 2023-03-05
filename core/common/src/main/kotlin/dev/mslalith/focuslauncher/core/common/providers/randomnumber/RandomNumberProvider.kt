package dev.mslalith.focuslauncher.core.common.providers.randomnumber

interface RandomNumberProvider {
    fun random(till: Int): Int
}
