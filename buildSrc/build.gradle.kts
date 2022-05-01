plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}
