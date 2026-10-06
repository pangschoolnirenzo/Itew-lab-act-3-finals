plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.tasknote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tasknote"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true // BuildConfig.DEBUG is used to switch HTTP body logging on/off
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")

    // Room (local database) - Week 11
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Navigation for Compose - Week 10
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ViewModel in Compose (gives us the viewModel() function with a factory) - Week 9 & 11
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // LiveData inside ViewModel - Week 9 & Week 13
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Lets Compose read LiveData with observeAsState() - Week 9 & Week 13
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    // collectAsStateWithLifecycle() - Week 14/15
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Networking & serialization - Week 14/15
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Unit tests
    testImplementation("junit:junit:4.13.2")
}
