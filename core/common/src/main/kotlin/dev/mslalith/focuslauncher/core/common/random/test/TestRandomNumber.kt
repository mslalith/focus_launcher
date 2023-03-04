package dev.mslalith.focuslauncher.core.common.random.test

import dev.mslalith.focuslauncher.core.common.random.RandomNumber
import javax.inject.Inject

class TestRandomNumber @Inject constructor() : RandomNumber {
    override fun random(till: Int): Int = 0
}
