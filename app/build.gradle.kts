plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.dojagy.todaysave"
    compileSdk = targetSdkVersion

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
            //signingConfig = signingConfigs.getByName("release")

            //manifestPlaceholders["enableCrashReporting"] = "true"
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            //manifestPlaceholders["enableCrashReporting"] = "false"
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
    implementation(project(":data:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.coil.compose)

    // Compose BOM을 먼저 플랫폼으로 가져옵니다.
    implementation(platform(libs.androidx.compose.bom))
    // compose-ui 번들을 사용하여 Compose 관련 라이브러리를 한 번에 추가합니다.
    implementation(libs.bundles.compose.ui)

    // network 번들을 사용하여 네트워킹 라이브러리를 한 번에 추가합니다.
    implementation(libs.bundles.network)

    // Hilt (DI)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Oss
    implementation(libs.play.services.oss.licenses)

    // Splash
    implementation(libs.androidx.splashscreen)

    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.compose.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}