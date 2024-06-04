plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //activate KSP plugin
    id("com.google.devtools.ksp")
    // Activate Parcelize Feature
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.fransbudikashira.storyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fransbudikashira.storyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Easiest way to handle Json Converter and API Service
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // Load Image from internet
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    // - - -
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    // Room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.0-rc01")
    ksp("androidx.room:room-compiler:2.6.1")
    // Fragment-KTX
    implementation("androidx.fragment:fragment-ktx:1.7.1")
    // Convert DateTime
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")
    //maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    //paging
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")
    //testing
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0") //InstantTaskExecutorRule
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0") //TestDispatcher
    testImplementation("androidx.arch.core:core-testing:2.2.0") //InstantTaskExecutorRule
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0") //TestDispatcher
    //mockito - - -
    testImplementation("org.mockito:mockito-core:5.6.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
}