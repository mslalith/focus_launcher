package dev.mslalith.focuslauncher.core.data.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.data.repository.AppDrawerRepo
import dev.mslalith.focuslauncher.core.data.repository.ClockRepo
import dev.mslalith.focuslauncher.core.data.repository.FavoritesRepo
import dev.mslalith.focuslauncher.core.data.repository.HiddenAppsRepo
import dev.mslalith.focuslauncher.core.data.repository.LunarPhaseDetailsRepo
import dev.mslalith.focuslauncher.core.data.repository.PlacesRepo
import dev.mslalith.focuslauncher.core.data.repository.QuotesRepo
import dev.mslalith.focuslauncher.core.data.repository.ThemeRepo
import dev.mslalith.focuslauncher.core.data.repository.impl.AppDrawerRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.ClockRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.FavoritesRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.HiddenAppsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.LunarPhaseDetailsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.PlacesRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.QuotesRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.ThemeRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.settings.AppDrawerSettingsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.settings.ClockSettingsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.settings.GeneralSettingsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.settings.LunarPhaseSettingsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.impl.settings.QuotesSettingsRepoImpl
import dev.mslalith.focuslauncher.core.data.repository.settings.AppDrawerSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.ClockSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.GeneralSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.LunarPhaseSettingsRepo
import dev.mslalith.focuslauncher.core.data.repository.settings.QuotesSettingsRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppDrawerRepo(repo: AppDrawerRepoImpl): AppDrawerRepo

    @Binds
    @Singleton
    abstract fun bindClockRepo(repo: ClockRepoImpl): ClockRepo

    @Binds
    @Singleton
    abstract fun bindFavoritesRepo(repo: FavoritesRepoImpl): FavoritesRepo

    @Binds
    @Singleton
    abstract fun bindHiddenAppsRepo(repo: HiddenAppsRepoImpl): HiddenAppsRepo

    @Binds
    @Singleton
    abstract fun bindLunarPhaseDetailsRepo(repo: LunarPhaseDetailsRepoImpl): LunarPhaseDetailsRepo

    @Binds
    @Singleton
    abstract fun bindPlacesRepo(repo: PlacesRepoImpl): PlacesRepo

    @Binds
    @Singleton
    abstract fun bindQuotesRepo(repo: QuotesRepoImpl): QuotesRepo

    @Binds
    @Singleton
    abstract fun bindThemeRepo(repo: ThemeRepoImpl): ThemeRepo

    @Binds
    @Singleton
    abstract fun bindAppDrawerSettingsRepo(repo: AppDrawerSettingsRepoImpl): AppDrawerSettingsRepo

    @Binds
    @Singleton
    abstract fun bindClockSettingsRepo(repo: ClockSettingsRepoImpl): ClockSettingsRepo

    @Binds
    @Singleton
    abstract fun bindGeneralSettingsRepo(repo: GeneralSettingsRepoImpl):  GeneralSettingsRepo

    @Binds
    @Singleton
    abstract fun bindLunarPhaseSettingsRepo(repo: LunarPhaseSettingsRepoImpl): LunarPhaseSettingsRepo

    @Binds
    @Singleton
    abstract fun bindQuotesSettingsRepo(repo: QuotesSettingsRepoImpl): QuotesSettingsRepo
}
