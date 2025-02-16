plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mapsplatform.secrets)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
}

// Directory for Room schemas
room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.sam.pokemondemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sam.pokemondemo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        metricsDestination = layout.buildDirectory.dir("compose_compiler")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.truth.test)
    testImplementation(libs.turbine.test)

    // gemini ai
    implementation(libs.gemini)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Timber
    implementation(libs.timber)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.constraintlayout.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.adapter)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.adapter)

    // Okhttp3
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    // Coil
    implementation(libs.coil.core)
    implementation(libs.coil.compose)

    // Flipper
    debugImplementation(libs.facebook.flipper)
    debugImplementation(libs.facebook.flipper.network)
    debugImplementation(libs.facebook.soloader)
    releaseImplementation(libs.facebook.flipper.noop)

    // Serialization
    implementation(libs.kotlin.serialization)

    // desugaring
    coreLibraryDesugaring(libs.android.desugaring)

    // paging
    implementation(libs.androidx.paging)
    implementation(libs.compose.paging)

    // room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
}
