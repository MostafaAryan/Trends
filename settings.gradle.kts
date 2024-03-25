pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // maven { url "https://maven.google.com" }
        // mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        // maven { url "https://maven.google.com" }
        // mavenLocal()
    }
}

rootProject.name = "Trends"
include(":app")
include(":features:trending")
include(":core:common")
include(":core:data")
include(":features:explore")
