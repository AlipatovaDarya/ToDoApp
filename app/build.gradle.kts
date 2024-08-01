
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.todoapp3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todoapp3"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-graphics-android:1.6.8")
    implementation("androidx.wear.compose:compose-material:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:2.8.3")
    implementation("androidx.tracing:tracing-perfetto-handshake:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.wear.compose:compose-material3:1.0.0-alpha23")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")

    //DatePicker
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    //Retrofit, Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //room
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    //DI - Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.fragment:fragment-ktx:1.8.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // SwipesRefresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.19.0")

    //divKit
    implementation("com.yandex.div:div:30.9.0")
    implementation("com.yandex.div:div-core:30.9.0")
    implementation("com.yandex.div:div-json:30.9.0")
    implementation("com.yandex.div:picasso:30.9.0")
    implementation("com.yandex.div:div-pinch-to-zoom:30.9.0")
    implementation("com.yandex.div:div-rive:30.9.0")
    implementation("com.yandex.div:glide:30.9.0")

    // dataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-preferences-core:1.1.1")



}