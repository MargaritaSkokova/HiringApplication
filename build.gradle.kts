// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("androidx.navigation.safeargs") version "2.8.3" apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}