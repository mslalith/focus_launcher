package dev.mslalith.focuslauncher.core.common.random.impl

import dev.mslalith.focuslauncher.core.common.random.RandomNumber
import java.util.Random
import javax.inject.Inject

internal class RandomNumberImpl @Inject constructor() : RandomNumber {
    override fun random(till: Int): Int = Random().nextInt(till)
}
