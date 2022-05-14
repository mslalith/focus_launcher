import org.gradle.api.artifacts.dsl.DependencyHandler

object Libs {
    const val buildToolsGradle = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val buildToolsKotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val buildToolsHiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
    const val buildToolsKotlinxKover = "org.jetbrains.kotlinx:kover:${Versions.KOTLINX_KOVER}"
    const val buildToolsKotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.KOTLIN_CORE_KTX}"
    const val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.KOTLINX_DATETIME}"
    const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES}"

    const val ktorClientCore = "io.ktor:ktor-client-core:${Versions.KTOR_CLIENT}"
    const val ktorClientAndroid = "io.ktor:ktor-client-android:${Versions.KTOR_CLIENT}"
    const val ktorClientSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.KTOR_CLIENT}"
    const val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.KTOR_CLIENT}"

    const val googleMaterial = "com.google.android.material:material:${Versions.GOOGLE_MATERIAL}"
    const val googlePlayAppUpdate = "com.google.android.play:app-update:${Versions.GOOGLE_PLAY_APP_UPDATE}"
    const val googlePlayAppUpdateKtx = "com.google.android.play:app-update-ktx:${Versions.GOOGLE_PLAY_APP_UPDATE}"
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

    const val thirdSunCalc = "org.shredzone.commons:commons-suncalc:${Versions.THIRD_SUNCALC}"

    const val testJUnit = "junit:junit:${Versions.TEST_JUNIT}"
    const val testTruth = "com.google.truth:truth:${Versions.TEST_TRUTH}"
    const val testAndroidXJUnit = "androidx.test.ext:junit:${Versions.TEST_ANDROIDX_JUNIT}"
    const val testAndroidXEspresso = "androidx.test.espresso:espresso-core:${Versions.TEST_ANDROIDX_ESPRESSO}"
    const val testComposeJUnit = "androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE}"
    const val testKotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.TEST_KOTLIN_COROUTINES}"
    const val testTurbine = "app.cash.turbine:turbine:${Versions.TEST_TURBINE}"
    const val testAndroidxCoreKtx = "androidx.test:core-ktx:${Versions.TEST_ANDROIDX_CORE_KTX}"
    const val testAndroidxCoreTesting = "androidx.arch.core:core-testing:${Versions.TEST_ANDROIDX_CORE_TESTING}"
    const val testRobolectric = "org.robolectric:robolectric:${Versions.TEST_ROBOLECTRIC}"
}

/**
 * Grouping methods
 */
fun DependencyHandler.kotlin() {
    implementation(Libs.kotlinxDateTime)
}

fun DependencyHandler.ktorClient() {
    implementation(Libs.ktorClientCore)
    implementation(Libs.ktorClientAndroid)
    implementation(Libs.ktorClientSerialization)
    implementation(Libs.ktorClientContentNegotiation)
}

fun DependencyHandler.compose() {
    implementation(Libs.composeMaterial)
    implementation(Libs.composeUi)
    implementation(Libs.composeUiTooling)
}

fun DependencyHandler.google() {
    implementation(Libs.googleMaterial)
}

fun DependencyHandler.playInAppUpdate() {
    implementation(Libs.googlePlayAppUpdate)
    implementation(Libs.googlePlayAppUpdateKtx)
}

fun DependencyHandler.androidx() {
    androidxCoreKtx()
    implementation(Libs.lifecycleRuntimeKtx)
    implementation(Libs.paletteKtx)
}

fun DependencyHandler.androidxCoreKtx() {
    implementation(Libs.coreKtx)
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
    hiltAndroid()

    kapt(Libs.hiltCompiler)
    implementation(Libs.hiltNavigationCompose)
}

fun DependencyHandler.hiltAndroid() {
    implementation(Libs.hiltAndroid)
    kapt(Libs.hiltAndroidCompiler)
}

fun DependencyHandler.room() {
    implementation(Libs.roomKtx)
    implementation(Libs.roomRuntime)
    ksp(Libs.roomCompiler)
}

fun DependencyHandler.accompanist() {
    implementation(Libs.accompanistPager)
    implementation(Libs.accompanistSystemUiController)
    implementation(Libs.accompanistInsets)
    implementation(Libs.accompanistFlowLayout)
}

fun DependencyHandler.thirdPartyLibs() {
    implementation(Libs.thirdSunCalc)
}

fun DependencyHandler.testLibs() {
    junit(includeAndroid = true)
    androidTestImplementation(Libs.testComposeJUnit)

    truth(includeAndroid = true)
    kotlinxCoroutinesTest(includeAndroid = true)
    turbine()
}

fun DependencyHandler.junit(includeAndroid: Boolean) {
    testImplementation(Libs.testJUnit)
    if (includeAndroid) {
        androidTestImplementation(Libs.testAndroidXJUnit)
        androidTestImplementation(Libs.testAndroidXEspresso)
    }
}

fun DependencyHandler.robolectric() {
    testImplementation(Libs.testAndroidxCoreKtx)
    testImplementation(Libs.testRobolectric)
}

fun DependencyHandler.truth(includeAndroid: Boolean) {
    testImplementation(Libs.testTruth)
    if (includeAndroid) androidTestImplementation(Libs.testTruth)
}

fun DependencyHandler.kotlinxCoroutinesTest(includeAndroid: Boolean) {
    testImplementation(Libs.testKotlinCoroutines)
    if (includeAndroid) androidTestImplementation(Libs.testKotlinCoroutines)
}

fun DependencyHandler.turbine(includeAndroid: Boolean = true) {
    testImplementation(Libs.testTurbine)
    if (includeAndroid) androidTestImplementation(Libs.testTurbine)
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

private fun DependencyHandler.ksp(depName: String) {
    add("ksp", depName)
}

private fun DependencyHandler.testImplementation(depName: String) {
    add("testImplementation", depName)
}

private fun DependencyHandler.androidTestImplementation(depName: String) {
    add("androidTestImplementation", depName)
}
