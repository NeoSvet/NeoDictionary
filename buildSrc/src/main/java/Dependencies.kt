object Versions {
    //Basic
    const val core_ktx = "1.6.0"
    const val appcompat = "1.3.1"
    const val material = "1.4.0"
    const val constraint = "2.1.0"

    //Coroutines
    const val coroutines = "1.5.2"

    //rxJava
    const val rx_java = "3.0.6"
    const val rx_android = "3.0.0"

    //Retrofit
    const val retrofit = "2.9.0"

    //Koin
    const val koin = "3.1.2"

    //Room
    const val room = "2.3.0"

    //Cicerone
    const val cicerone = "7.1"

    //Coil
    const val coil = "0.11.0"
}

object Basic {
    const val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
}

object Coroutines {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val c_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
}

object RxJava {
    const val rxjava = "io.reactivex.rxjava3:rxjava:${Versions.rx_java}"
    const val rxandroid = "io.reactivex.rxjava3:rxandroid:${Versions.rx_android}"
}

object Retrofit {
    const val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val adapter_rxjava = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
}

object Koin {
    const val core = "io.insert-koin:koin-core:${Versions.koin}"
    const val koin_android = "io.insert-koin:koin-android:${Versions.koin}"
    const val compat = "io.insert-koin:koin-android-compat:${Versions.koin}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"
    const val kapt_compiler = "androidx.room:room-compiler:${Versions.room}"
}

object Cicerone {
    const val core = "com.github.terrakok:cicerone:${Versions.cicerone}"
}

object Coil {
    const val core = "io.coil-kt:coil:${Versions.coil}"
}
