package dev.mslalith.focuslauncher.core.testing

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before

abstract class SystemUnderTest<T> : CoroutineTest() {

    protected val context: Context = ApplicationProvider.getApplicationContext()
    protected val systemUnderTest: T by lazy { provideSystemUnderTest(context) }

    abstract fun provideSystemUnderTest(context: Context): T

    @Before
    open fun setUp() = Unit

    @After
    open fun tearDown() = Unit
}
