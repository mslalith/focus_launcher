package dev.mslalith.focuslauncher.core.settings.sentry.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mslalith.focuslauncher.core.settings.sentry.SentrySettings
import dev.mslalith.focuslauncher.core.settings.sentry.SentrySettingsImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SentryModule {

    @Binds
    @Singleton
    abstract fun bindSentrySettings(sentrySettingsImpl: SentrySettingsImpl): SentrySettings
}
