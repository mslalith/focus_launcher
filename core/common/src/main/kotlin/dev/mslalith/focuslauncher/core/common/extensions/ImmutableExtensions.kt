package dev.mslalith.focuslauncher.core.common.extensions

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList

inline fun <T, K> Iterable<T>.groupByImmutable(
    keySelector: (T) -> K
): ImmutableMap<K, ImmutableList<T>> {
    var persistentMap = persistentMapOf<K, ImmutableList<T>>()
    groupBy(keySelector = keySelector).forEach { (key, value) ->
        persistentMap = persistentMap.put(key, value.toImmutableList())
    }
    return persistentMap
}
