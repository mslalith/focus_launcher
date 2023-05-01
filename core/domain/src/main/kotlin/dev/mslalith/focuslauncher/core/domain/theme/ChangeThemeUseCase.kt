package dev.mslalith.focuslauncher.core.domain.theme

import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.model.Theme
import javax.inject.Inject

class ChangeThemeUseCase @Inject constructor(
    private val themeRepo: ThemeRepo
) {
    suspend operator fun invoke(theme: Theme) = themeRepo.changeTheme(theme = theme)
}
