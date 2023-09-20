import com.google.devtools.ksp.gradle.KspTaskJvm

plugins {
    id("idea")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.squareup.wire")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.dcns.dailycost"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dcns.dailycost"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0-alpha01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    testOptions {
//        unitTests {
//            isIncludeAndroidResources = true
//            isReturnDefaultValues = true
//        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            buildConfigField("String", "API_BASE_URL", "\"https://dailycost.my.id/\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            kotlinOptions {
                freeCompilerArgs += listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xjvm-default=all"
                )
            }
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true

            buildConfigField("String", "API_BASE_URL", "\"https://dailycost.my.id/\"")

            kotlinOptions {
                freeCompilerArgs += listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xjvm-default=all"
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

androidComponents {
	onVariants { variant ->
		// https://github.com/square/wire/issues/2335
		val buildType = variant.buildType.toString()
		val flavor = variant.flavorName.toString()
		tasks.withType<KspTaskJvm> {
			if (name.contains(buildType, ignoreCase = true) && name.contains(flavor, ignoreCase = true)) {
				dependsOn("generate${flavor.capitalize()}${buildType.capitalize()}Protos")
			}
		}
	}
}

wire {
    kotlin {
        android = true
    }
}

dependencies {

    val kotlin_version by extra("1.9.0")
    val compose_version by extra("1.5.0")
    val lifecycle_version by extra("2.6.2")
    val accompanist_version by extra("0.32.0")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

	implementation(project(mapOf("path" to ":datemodule")))

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
	kapt ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.runtime:runtime:1.5.1")
    implementation("androidx.compose.runtime:runtime-livedata:${extra["compose_version"]}")
    implementation("androidx.navigation:navigation-compose:2.7.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
	implementation("androidx.core:core-splashscreen:1.0.1")

    // Work Manager
    implementation("androidx.hilt:hilt-work:1.1.0-alpha01")
    implementation("androidx.work:work-runtime:2.8.1")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.work:work-multiprocess:2.8.1")

    // Compose Common
    implementation("androidx.compose.ui:ui:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${extra["compose_version"]}")
    implementation("androidx.compose.foundation:foundation:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-util:${extra["compose_version"]}")
    implementation("androidx.compose.animation:animation:${extra["compose_version"]}")

    // Compose Android
    implementation("androidx.compose.ui:ui-android:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview-android:${extra["compose_version"]}")
    implementation("androidx.compose.foundation:foundation-android:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-util-android:${extra["compose_version"]}")
    implementation("androidx.compose.animation:animation-android:${extra["compose_version"]}")

    // Constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha12")

    // Biometric
    implementation("androidx.biometric:biometric-ktx:1.2.0-alpha05")

    // Material Design
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.compose.material:material-icons-extended:1.5.1")
    implementation("androidx.compose.material3:material3-android:1.2.0-alpha07")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.1")

    // Large screen support
    implementation("androidx.window:window:1.1.0")

    // Datastore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-core:1.0.0")
//    implementation("com.google.protobuf:protobuf-javalite:3.18.0")
//    implementation("com.google.protobuf:protobuf-kotlin:3.19.1")
//    implementation("com.google.protobuf:protobuf-kotlin-lite:3.19.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${extra["lifecycle_version"]}")
	implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${extra["lifecycle_version"]}")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${extra["lifecycle_version"]}")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt("androidx.lifecycle:lifecycle-common-java8:${extra["lifecycle_version"]}")

    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("androidx.hilt:hilt-compiler:1.1.0-alpha01")
    ksp("com.google.dagger:hilt-compiler:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")

    // Room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

	// Firebase
	implementation("com.google.firebase:firebase-crashlytics-ktx:18.4.1")
	implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
	implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")

    // Accompanist
    implementation("com.google.accompanist:accompanist-pager:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-adaptive:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-placeholder:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-navigation-material:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-navigation-animation:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-flowlayout:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-permissions:${extra["accompanist_version"]}")
    implementation("com.google.accompanist:accompanist-systemuicontroller:${extra["accompanist_version"]}")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")

    // Other
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.squareup.wire:wire-runtime:4.4.3")
    implementation("com.maxkeppeler.sheets-compose-dialogs:state:1.2.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation("com.github.omercemcicekli:CardStack:0.0.6")
    implementation("com.github.marlonlom:timeago:4.0.3")
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    implementation("com.intuit.sdp:sdp-android:1.1.0")

    implementation("commons-codec:commons-codec:1.16.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")

    testImplementation("org.json:json:20180813")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestUtil("androidx.test:orchestrator:1.4.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${extra["compose_version"]}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44.2")
//	debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12") // Memory leak checker library
    debugImplementation("androidx.compose.ui:ui-tooling:${extra["compose_version"]}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${extra["compose_version"]}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
