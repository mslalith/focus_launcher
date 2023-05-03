package dev.mslalith.focuslauncher.core.common.extensions

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

inline fun <T, K> Iterable<T>.groupByImmutable(
    keySelector: (T) -> K
): ImmutableMap<K, ImmutableList<T>> = persistentMapOf<K, ImmutableList<T>>().apply {
    groupBy(keySelector = keySelector).forEach { (key, value) ->
        put(key, value.toImmutableList())
    }
}.toImmutableMap()
