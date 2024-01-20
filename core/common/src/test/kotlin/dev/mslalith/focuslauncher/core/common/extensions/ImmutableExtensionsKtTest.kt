package dev.mslalith.focuslauncher.core.common.extensions

import com.google.common.truth.Truth.assertThat
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.junit.Test

class ImmutableExtensionsKtTest {

    @Test
    fun `01 - group by first character`() {
        val list = listOf("abc", "acc", "bcc", "bcd", "cdc")
        val expected = persistentMapOf(
            'a' to persistentListOf("abc", "acc"),
            'b' to persistentListOf("bcc", "bcd"),
            'c' to persistentListOf("cdc")
        )
        val actual = list.groupByImmutable { it.first() }
        assertThat(actual).isEqualTo(expected)
    }
}
