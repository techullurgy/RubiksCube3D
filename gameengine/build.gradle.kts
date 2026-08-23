import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
}

kotlin {
    iosArm64()
    iosSimulatorArm64()
    jvm()

    js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    android {
        namespace = "com.techullurgy.gameengine"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.graphics.math)
        }
    }
}