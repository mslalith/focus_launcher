package dev.mslalith.focuslauncher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.mslalith.focuslauncher.core.lint.kover.IgnoreInKoverReport

@HiltAndroidApp
@IgnoreInKoverReport
class FocusLauncherApp : Application()
