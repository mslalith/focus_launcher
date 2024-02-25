package dev.mslalith.focuslauncher.core.common.extensions

import com.google.common.truth.Truth.assertThat
import dev.mslalith.focuslauncher.core.testing.extensions.withLocale
import org.junit.Test
import java.util.Locale

class FormatNumberTest {

    private fun limitDecimalTests() {
        assertThat(0.0.limitDecimals(maxFractions = 4)).isEqualTo("0.00")
        assertThat(23.0.limitDecimals(maxFractions = 4)).isEqualTo("23.00")

        assertThat(1.23.limitDecimals(maxFractions = 4)).isEqualTo("1.23")
        assertThat(1.2345678.limitDecimals(maxFractions = 4)).isEqualTo("1.2346")

        assertThat(753.23.limitDecimals(maxFractions = 4)).isEqualTo("753.23")
        assertThat(463.2345678.limitDecimals(maxFractions = 4)).isEqualTo("463.2346")
    }

    @Test
    fun `limit decimal in US locale`() = withLocale(Locale.US) { limitDecimalTests() }

    @Test
    fun `limit decimal in German locale`() = withLocale(Locale.GERMANY) { limitDecimalTests() }
}
