plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "ru.neosvet.dictionary"
        minSdk = 23
        targetSdk = 30
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
    implementation(project(":neoflickr"))
    implementation(project(":utils"))
    implementation(Basic.core_ktx)
    implementation(Basic.appcompat)
    implementation(Basic.material)
    implementation(Basic.constraint)
    implementation(RxJava.rxjava)
    implementation(RxJava.rxandroid)
    implementation(Retrofit.core)
    implementation(Retrofit.converter_gson)
    implementation(Retrofit.adapter_rxjava)
    implementation(Koin.core)
    implementation(Koin.koin_android)
    implementation(Koin.compat)
    implementation(Room.runtime)
    implementation(Room.room_ktx)
    kapt(Room.kapt_compiler)
    implementation(Cicerone.core)
    implementation(Coil.core)
}