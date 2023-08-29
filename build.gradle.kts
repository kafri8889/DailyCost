buildscript {
    val kotlin_version by extra("1.9.0")
    val compose_version by extra("1.5.0")
    val lifecycle_version by extra("2.6.1")
    val accompanist_version by extra("0.32.0")

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0-beta01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath("com.squareup.wire:wire-gradle-plugin:4.4.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0-beta01" apply false
    id("com.android.library") version "8.2.0-beta01" apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}
tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}
