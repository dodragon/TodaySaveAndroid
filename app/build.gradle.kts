plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.aboutlibraries)
}

val keyStoreProp = getProperties("keystore.properties")

android {
    namespace = "com.dojagy.todaysave"
    compileSdk = targetSdkVersion

    signingConfigs {
        create("release") {
            storeFile = file(keyStoreProp.getProperty("KEY_STORE_FILE"))
            keyAlias = keyStoreProp.getProperty("KEY_STORE_ALIAS")
            keyPassword = keyStoreProp.getProperty("KEY_PASSWORD")
            storePassword = keyStoreProp.getProperty("KEY_PASSWORD")
        }
    }

    defaultConfig {
        applicationId = "com.dojagy.todaysave"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        versionCode = todaySaveVersionCode
        versionName = todaySaveVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

            manifestPlaceholders["enableCrashReporting"] = "true"
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            manifestPlaceholders["enableCrashReporting"] = "false"
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
        buildConfig = true
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:deprecation")
}

dependencies {
    // Core Modules
    implementation(project(":core:resources"))
    implementation(project(":core:common"))
    implementation(project(":core:common-android"))
    implementation(project(":core:view"))

    // Data Modules
    implementation(project(":data:model"))
    implementation(project(":data:source"))
    implementation(project(":data:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)

    // network 번들을 사용하여 네트워킹 라이브러리를 한 번에 추가합니다.
    implementation(libs.bundles.network)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // Oss
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose)

    // kakao
    implementation(libs.kakao.user)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.compose.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}