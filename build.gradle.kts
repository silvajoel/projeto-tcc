plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false // já cobre org.jetbrains.kotlin.android
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
    kotlin("jvm") version "1.9.24" apply false
    kotlin("plugin.serialization") version "1.9.24" apply false
}