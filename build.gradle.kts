// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    // alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.org.jetbrains.kotlin.serialization) apply false
}
true // Needed to make the Suppress annotation work for the plugins block


subprojects {
    // Accessing the `PluginContainer` in order to use `whenPluginAdded` function
    project.plugins.applyBaseConfig(project)
}

/**
 * Apply configuration settings that are shared across all modules.
 */
fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is com.android.build.gradle.AppPlugin -> {
                project.extensions
                    .getByType<com.android.build.gradle.AppExtension>()
                    .apply {
                        applyCommons()
                    }
            }
            is com.android.build.gradle.LibraryPlugin -> {
                project.extensions
                    .getByType<com.android.build.gradle.LibraryExtension>()
                    .apply {
                        applyCommons()
                    }
            }
        }
    }
}

fun com.android.build.gradle.AppExtension.applyCommons() {
    baseConfig()
}

fun com.android.build.gradle.LibraryExtension.applyCommons() {
    baseConfig()
}

fun com.android.build.gradle.BaseExtension.baseConfig() {

    //compileSdkVersion(AppConfig.compileSdk)

    defaultConfig.apply {
        //minSdk = AppConfig.minSdk
        //targetSdk = AppConfig.targetSdk

        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions.apply {
        // sourceCompatibility = AppConfig.CompileOptions.javaSourceCompatibility
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            // jvmTarget = AppConfig.CompileOptions.kotlinJvmTarget
            jvmTarget = "17"
        }
    }
}