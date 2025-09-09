import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.gentalha.code.madeinlab"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.gentalha.code.madeinlab"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // region Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // endregion

    // region Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    // endregion

    // region Koin
    implementation(libs.bundles.koin)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)
    // endregion

    // region Test
    testImplementation(libs.bundles.unit.test)
    // region Compose Test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui.test)
    androidTestImplementation(libs.bundles.instrumentedTest)
    debugImplementation(libs.compose.ui.test.manifest)
    // endregion
    // endregion
}