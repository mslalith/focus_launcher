package dev.mslalith.focuslauncher.core.domain.theme

import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.model.Theme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val themeRepo: ThemeRepo
) {
    operator fun invoke(): Flow<Theme> = themeRepo.currentThemeFlow
}
