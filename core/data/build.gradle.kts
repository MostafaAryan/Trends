@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt.android)
    // alias(libs.plugins.org.jetbrains.kotlin.kapt)
    kotlin("kapt")
    alias(libs.plugins.org.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.programmerofpersia.trends.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    // implementation(libs.appcompat)
    // implementation(libs.material)

    // hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)

    // retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    // kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)

    // coroutines and flow
    implementation(libs.kotlinx.coroutines.android)

    //
    implementation(libs.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}