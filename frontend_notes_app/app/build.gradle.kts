plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.frontendnotesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.frontendnotesapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    // Enable Jetpack Compose and explicitly set the Compose compiler version to match Compose BOM 2024.03.00 (maps to 1.5.11)
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Jetpack Compose and related dependencies (unified versions)
    val composeBom = platform("androidx.compose:compose-bom:2024.03.00")
    implementation(composeBom)
    implementation("androidx.activity:activity-compose:1.8.2")

    // Align all lifecycle libraries to 2.7.0 for Compose 1.6.x compatibility
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Ktor and other utilities
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7")
    implementation("io.ktor:ktor-client-serialization:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.russhwolf:multiplatform-settings:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // Logging
    implementation("io.github.microutils:kotlin-logging:3.0.5")
}
