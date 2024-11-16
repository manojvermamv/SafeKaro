plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinParcelize)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinKapt)
}

android {
    namespace = "com.safekaro.partner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.safekaro.partner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // default resources values (don't remove resValue 'app_name' because it used by project)
        resValue("string", "app_name", "Safe Karo")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://test.safekaro.com/api/\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            buildConfigField("String", "BASE_URL", "\"https://test.safekaro.com/api/\"")
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)

    implementation(libs.koin.core)
    implementation(libs.koin.android)

    implementation(libs.jetbrainsKotlinReflect)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // - - Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    // - - Lifecycle Extensions and ViewModel
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")

    // Google dependencies
    implementation("com.google.android.recaptcha:recaptcha:18.4.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.2")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    // - - Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.3.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.1")

    // - - Image Loading
    implementation("com.squareup.picasso:picasso:2.71828")

    // - - Ui-UX
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.github.IslamKhSh:CardSlider:1.0.1")
    implementation("io.github.chaosleung:pinview:1.4.4")
    implementation("com.github.Ajinkrishnak:CountryCodePicker:1.0.2")
    implementation("com.github.afsalkodasseri:DateRangePicker:1.0.1")

    // Extra
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.0")
}