package dev.mslalith.focuslauncher.core.model.extensions

fun <T : Any?> List<T>.generateHashCode(): Int {
    if (isEmpty()) return hashCode()

    var result = first().hashCode()
    drop(n = 1).forEach {
        result = 31 * result + it.hashCode()
    }
    return result
}
