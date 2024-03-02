package dev.mslalith.focuslauncher.core.settings.sentry.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.mslalith.focuslauncher.core.settings.sentry.FakeSentrySettings
import dev.mslalith.focuslauncher.core.settings.sentry.SentrySettings
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SentryModule::class]
)
internal object TestSentryModule {

    @Provides
    @Singleton
    fun provideSentrySettings(): SentrySettings = FakeSentrySettings()
}
