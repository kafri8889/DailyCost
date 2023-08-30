plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("kotlin-parcelize")
}

android {
	namespace = "com.anafthdev.datemodule"
	compileSdk = 33
	
	defaultConfig {
		minSdk = 24
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
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
		isCoreLibraryDesugaringEnabled = true
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	
	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
	
	implementation("androidx.core:core-ktx:1.10.1")
	implementation("androidx.appcompat:appcompat:1.6.1")

	implementation("com.google.code.gson:gson:2.9.0")
	
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}