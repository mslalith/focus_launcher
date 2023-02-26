package dev.mslalith.focuslauncher.core.common.random

import java.util.Random
import javax.inject.Inject

internal class RandomNumberImpl @Inject constructor() : RandomNumber {
    override fun random(till: Int): Int = Random().nextInt(till)
}
