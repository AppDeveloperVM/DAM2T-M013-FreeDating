plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

val supportVersion by extra ("28.0.0")
val roomVersion by extra ("2.1.0")
val retrofitVersion by extra ("2.3.0")
val glideVersion by extra ("4.9.0")
val rxJavaVersion by extra ("2.1.1")

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "cat.smartcoding.mendez.freedating"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // default
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.0")
    // Room
    implementation("androidx.room:room-runtime:$roomVersion")
//    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-rxjava2:$roomVersion")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    // Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    // Palette
    implementation("com.android.support:palette-v7:$supportVersion")
    // RxJava2
    implementation("io.reactivex.rxjava2:rxjava:$rxJavaVersion")
    implementation("io.reactivex.rxjava2:rxandroid:$rxJavaVersion")
    implementation("com.google.firebase:firebase-storage-ktx:20.0.0")

    //Firebase
    /*implementation ("com.google.firebase:firebase-bom:29.0.0")
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation ("com.google.firebase:firebase-database-ktx:20.0.2")
    implementation ("com.google.firebase:firebase-storage-ktx:20.0.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.0.1")
*/
    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")


    // BoM Bill of Material to avoid specifying individual library versions
    implementation(platform("com.google.firebase:firebase-bom:29.0.0"))
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")
    // Firebase Analytics: Usage metrics
    implementation("com.google.firebase:firebase-analytics-ktx")
    // Firebase Database: Realtime db
    implementation("com.google.firebase:firebase-database-ktx")
    // Firebase Firestore: is a flexible, scalable database for mobile, web, and server development from Firebase and Google Cloud. Similar to Firebase Realtime Database
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Firebase Functions: a serverless framework that lets you automatically run backend code in response to events triggered by Firebase
    implementation("com.google.firebase:firebase-functions-ktx")

    implementation("androidx.gridlayout:gridlayout:1.0.0")
    // Firebase Cloud Messaging: a cross-platform messaging solution that lets you reliably send messages at no cost.
    //implementation("com.google.firebase:firebase-messaging-ktx")
    // Firebase Storage: dependencia to store files in Firebase, images...
    //implementation("com.google.firebase:firebase-storage-ktx")
    // Firebase Crashlytics: is a lightweight, realtime crash reporter that helps you track, prioritize, and fix stability issues
    //implementation("com.google.firebase:firebase-crashlytics-ktx")
    // Firebase Dynamic Links: users can be taken directly to the linked content in your native app
    //implementation("com.google.firebase:firebase-dynamic-links-ktx")
    // Firebase In Apps Messaging: helps you engage your app's active users by sending them targeted, contextual messages that encourage them to use key app features
    //implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    // Firebase ML: Machine learning
    //implementation("com.google.firebase:firebase-ml-modeldownloader-ktx")
    // Firebase Performance Monitoring: is a service that helps you to gain insight into the performance characteristics of your Apple, Android, and web apps
    //implementation("com.google.firebase:firebase-perf-ktx")
    // Firebase Config: is a cloud service that lets you change the behavior and appearance of your app without requiring users to download an app update.
    //implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.android.gms:play-services-auth:19.2.0")
    implementation("com.facebook.android:facebook-android-sdk:[4,5]")

    implementation ("androidx.appcompat:appcompat:1.3.0-beta01")
    implementation ("androidx.cardview:cardview:1.0.0")


}