import org.gradle.api.artifacts.dsl.DependencyHandler

object Libs {
    const val buildToolsGradle = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val buildToolsKotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val buildToolsHiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.KOTLIN_CORE_KTX}"
    const val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.KOTLINX_DATETIME}"

    const val googleMaterial = "com.google.android.material:material:${Versions.GOOGLE_MATERIAL}"
    const val googlePlayCoreKtx = "com.google.android.play:core-ktx:${Versions.GOOGLE_PLAY_CORE_KTX}"
    const val paletteKtx = "androidx.palette:palette-ktx:${Versions.PALETTE_KTX}"

    const val composeMaterial = "androidx.compose.material:material:${Versions.COMPOSE}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME_KTX}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}"
    const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:${Versions.CONSTRAINT_LAYOUT_COMPOSE}"
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.NAVIGATION_COMPOSE}"
    const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.LIFECYCLE_VIEWMODEL_COMPOSE}"

    const val dataStore = "androidx.datastore:datastore:${Versions.DATASTORE}"
    const val dataStorePreferences = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"

    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"

    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.HILT_COMPILER}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.HILT_NAVIGATION_COMPOSE}"

    const val roomKtx = "androidx.room:room-ktx:${Versions.ROOM}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.ROOM}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.ROOM}"

    const val accompanistPager = "com.google.accompanist:accompanist-pager:${Versions.ACCOMPANIST}"
    const val accompanistSystemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.ACCOMPANIST}"
    const val accompanistInsets = "com.google.accompanist:accompanist-insets:${Versions.ACCOMPANIST}"
    const val accompanistFlowLayout = "com.google.accompanist:accompanist-flowlayout:${Versions.ACCOMPANIST}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"

    const val thirdSunCalc = "org.shredzone.commons:commons-suncalc:${Versions.THIRD_SUNCALC}"

    const val testJUnit = "junit:junit:${Versions.TEST_JUNIT}"
    const val testTruth = "com.google.truth:truth:${Versions.TEST_TRUTH}"
    const val testAndroidXJUnit = "androidx.test.ext:junit:${Versions.TEST_ANDROIDX_JUNIT}"
    const val testAndroidXEspresso = "androidx.test.espresso:espresso-core:${Versions.TEST_ANDROIDX_ESPRESSO}"
    const val testComposeJUnit = "androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE}"
    const val testKotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.TEST_KOTLIN_COROUTINES}"
    const val testTurbine = "app.cash.turbine:turbine:${Versions.TEST_TURBINE}"
}

/**
 * Grouping methods
 */
fun DependencyHandler.kotlin() {
    implementation(Libs.kotlinxDateTime)
}

fun DependencyHandler.compose() {
    implementation(Libs.composeMaterial)
    implementation(Libs.composeUi)
    implementation(Libs.composeUiTooling)
}

fun DependencyHandler.google() {
    implementation(Libs.googleMaterial)
    implementation(Libs.googlePlayCoreKtx)
}

fun DependencyHandler.androidx() {
    implementation(Libs.coreKtx)
    implementation(Libs.lifecycleRuntimeKtx)
    implementation(Libs.paletteKtx)
}

fun DependencyHandler.composeInterop() {
    implementation(Libs.activityCompose)
    implementation(Libs.constraintLayoutCompose)
    implementation(Libs.navigationCompose)
    implementation(Libs.lifecycleViewModelCompose)
}

fun DependencyHandler.dataStore() {
    implementation(Libs.dataStore)
    implementation(Libs.dataStorePreferences)
}

fun DependencyHandler.hilt() {
    implementation(Libs.hiltAndroid)
    kapt(Libs.hiltAndroidCompiler)

    kapt(Libs.hiltCompiler)
    implementation(Libs.hiltNavigationCompose)
}

fun DependencyHandler.room() {
    implementation(Libs.roomKtx)
    implementation(Libs.roomRuntime)
    kapt(Libs.roomCompiler)
}

fun DependencyHandler.accompanist() {
    implementation(Libs.accompanistPager)
    implementation(Libs.accompanistSystemUiController)
    implementation(Libs.accompanistInsets)
    implementation(Libs.accompanistFlowLayout)
}

fun DependencyHandler.retrofit() {
    implementation(Libs.retrofit)
    implementation(Libs.retrofitGsonConverter)
}

fun DependencyHandler.thirdPartyLibs() {
    implementation(Libs.thirdSunCalc)
}

fun DependencyHandler.testLibs() {
    testImplementation(Libs.testJUnit)
    androidTestImplementation(Libs.testAndroidXJUnit)
    androidTestImplementation(Libs.testAndroidXEspresso)
    androidTestImplementation(Libs.testComposeJUnit)

    testImplementation(Libs.testTruth)
    androidTestImplementation(Libs.testTruth)
    testImplementation(Libs.testKotlinCoroutines)
    androidTestImplementation(Libs.testKotlinCoroutines)
    testImplementation(Libs.testTurbine)
    androidTestImplementation(Libs.testTurbine)
}


/**
 * Helper methods
 */
private fun DependencyHandler.implementation(depName: String) {
    add("implementation", depName)
}

private fun DependencyHandler.kapt(depName: String) {
    add("kapt", depName)
}

private fun DependencyHandler.testImplementation(depName: String) {
    add("testImplementation", depName)
}

private fun DependencyHandler.androidTestImplementation(depName: String) {
    add("androidTestImplementation", depName)
}
