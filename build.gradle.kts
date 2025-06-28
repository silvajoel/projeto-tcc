plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}