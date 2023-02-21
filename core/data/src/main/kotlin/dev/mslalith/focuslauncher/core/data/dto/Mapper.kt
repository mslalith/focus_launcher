package dev.mslalith.focuslauncher.core.data.dto

internal interface Mapper<I, O> {
    fun fromEntity(data: I): O
    fun toEntity(data: O): I
}
