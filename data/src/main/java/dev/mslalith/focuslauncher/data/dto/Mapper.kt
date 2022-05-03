package dev.mslalith.focuslauncher.data.dto

interface Mapper<I, O> {
    fun fromEntity(data: I): O
    fun toEntity(data: O): I
}
