plugins {
    id("com.android.library")
    kotlin("android")
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
    viewBinding {
        android.buildFeatures.viewBinding = true
    }
}

dependencies {
    implementation(Basic.core_ktx)
    implementation(Basic.appcompat)
    implementation(Basic.material)
    implementation(RxJava.rxjava)
    implementation(RxJava.rxandroid)
    implementation(Retrofit.core)
    implementation(Retrofit.converter_gson)
    implementation(Retrofit.adapter_rxjava)
    implementation(Coil.core)
}