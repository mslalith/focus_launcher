package dev.mslalith.focuslauncher.core.common.providers.randomnumber.test

import dev.mslalith.focuslauncher.core.common.providers.randomnumber.RandomNumber
import javax.inject.Inject

class TestRandomNumber @Inject constructor() : RandomNumber {
    private var randomNumber = 0

    override fun random(till: Int): Int = randomNumber

    fun setRandomNumber(randomNumber: Int) {
        this.randomNumber = randomNumber
    }
}
