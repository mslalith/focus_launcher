package dev.mslalith.focuslauncher.core.common.providers.randomnumber.impl

import dev.mslalith.focuslauncher.core.common.providers.randomnumber.RandomNumberProvider
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport
import java.util.Random
import javax.inject.Inject

@IgnoreInKoverReport
internal class RandomNumberProviderImpl @Inject constructor() : RandomNumberProvider {
    override fun random(till: Int): Int = Random().nextInt(till)
}
