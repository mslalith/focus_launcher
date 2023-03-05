package dev.mslalith.focuslauncher.core.common.providers.randomnumber.impl

import dev.mslalith.focuslauncher.core.common.providers.randomnumber.RandomNumber
import java.util.Random
import javax.inject.Inject

internal class RandomNumberImpl @Inject constructor() : RandomNumber {
    override fun random(till: Int): Int = Random().nextInt(till)
}
