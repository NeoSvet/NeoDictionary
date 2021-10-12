plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "ru.neosvet.dictionary"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":dictionary"))
    implementation(project(":neoflickr"))
    implementation(project(":utils"))
    implementation(Basic.core_ktx)
    implementation(Basic.appcompat)
    implementation(Basic.material)
    implementation(Basic.constraint)
    implementation(Basic.splashscreen)
    implementation(Room.runtime)
    implementation(Room.room_ktx)
    implementation(Koin.core)
    implementation(Koin.koin_android)
    implementation(Koin.compat)
    implementation(Coroutines.core)
    implementation(Coroutines.c_android)
    implementation(Cicerone.core)
}