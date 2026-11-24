import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// TMDB key lives in local.properties (gitignored): TMDB_API_KEY=your_key
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

android {
    namespace = "com.itismob.s03.groupiseight.adapted"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.itismob.s03.groupiseight.adapted"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "TMDB_API_KEY", "\"${localProps.getProperty("TMDB_API_KEY", "")}\"")
        // Optional: avoids Google Books anonymous-quota errors (HTTP 429). local.properties: GOOGLE_BOOKS_API_KEY=...
        buildConfigField("String", "GOOGLE_BOOKS_API_KEY", "\"${localProps.getProperty("GOOGLE_BOOKS_API_KEY", "")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.glide)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}