plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 23
        targetSdk = 30
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
    implementation(project(":utils"))
    implementation(Basic.core_ktx)
    implementation(RxJava.rxjava)
    implementation(RxJava.rxandroid)
    implementation(Retrofit.core)
    implementation(Retrofit.converter_gson)
    implementation(Retrofit.adapter_rxjava)
    implementation(Coroutines.core)
    implementation(Coroutines.c_android)
    implementation(Room.runtime)
    implementation(Room.room_ktx)
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    kapt(Room.kapt_compiler)
}