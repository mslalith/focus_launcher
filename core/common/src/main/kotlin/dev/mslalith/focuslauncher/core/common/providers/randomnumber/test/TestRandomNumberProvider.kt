package dev.mslalith.focuslauncher.core.common.providers.randomnumber.test

import dev.mslalith.focuslauncher.core.common.providers.randomnumber.RandomNumberProvider
import javax.inject.Inject

class TestRandomNumberProvider @Inject constructor() : RandomNumberProvider {
    private var randomNumber = 0

    override fun random(till: Int): Int = randomNumber

    fun setRandomNumber(randomNumber: Int) {
        this.randomNumber = randomNumber
    }
}
