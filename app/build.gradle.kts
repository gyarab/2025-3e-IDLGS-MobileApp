import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
}

@Suppress("PropertyName")
val LM_STUDIO_URL = "LM_STUDIO_URL"

fun appVersionName(): String {
	val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.DDD"))
	return "a$date"
}

android {
	namespace = "cz.idlgs.mobile"
	compileSdk {
		version = release(36)
	}

	defaultConfig {
		applicationId = namespace
		minSdk = 24
		targetSdk = 36
		versionCode = 1
		versionName = appVersionName()

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		val properties = Properties()
		val localPropertiesFile = project.rootProject.file("local.properties")
		if (localPropertiesFile.exists())
			properties.load(localPropertiesFile.inputStream())

		val lmStudioUrl = properties.getProperty(LM_STUDIO_URL) ?: ""
		buildConfigField("String", LM_STUDIO_URL, lmStudioUrl)
	}

	buildTypes {
		debug {
			applicationIdSuffix = ".dev"
		}
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
	implementation(libs.androidx.datastore.preferences)
	implementation(libs.androidx.compose.material.icons.extended)

	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
	implementation(libs.okhttp)
	implementation(libs.gson)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.tooling)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_11)
	}
}
