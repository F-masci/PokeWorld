plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
}

android {
    namespace = "it.fale.pokeworld"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.fale.pokeworld"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    //libreria Coil per compose per mostrare immagini dato l'url
    implementation(libs.coil.compose)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /** --- ROOM --- **/

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    /** --- GSON --- **/

    implementation("com.google.code.gson:gson:2.11.0")

    /** --- COIL --- **/

    implementation ("io.coil-kt:coil-compose:2.0.0")
    implementation("io.coil-kt:coil-gif:2.1.0")

    /** --- NAVIGATION --- **/
    implementation("androidx.navigation:navigation-compose:2.5.0")

    /** --- HILT --- **/
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    /** ---MODALE--- **/
    implementation ("androidx.compose.material3:material3:1.0.1")
    implementation ("androidx.compose.material:material:1.4.3")

    /** ---MODALE--- **/
//    implementation ("androidx.compose.material:material:1.4.0")
    implementation ("androidx.compose.ui:ui:1.4.0")
    implementation ("androidx.compose.foundation:foundation:1.4.0")
  //  implementation ("androidx.compose.material3:material3:1.0.0")
    implementation ("androidx.compose.material3:material3:1.1.0")

    implementation("com.google.accompanist:accompanist-drawablepainter:0.34.0")


    implementation ("androidx.compose.ui:ui:1.5.0")

    implementation ("androidx.compose.runtime:runtime-livedata:1.5.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.compose.material:material:1.5.0")

}