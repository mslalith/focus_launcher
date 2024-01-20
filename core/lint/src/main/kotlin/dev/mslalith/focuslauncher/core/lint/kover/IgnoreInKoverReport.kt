package dev.mslalith.focuslauncher.core.lint.kover

@Retention(value = AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
annotation class IgnoreInKoverReport
