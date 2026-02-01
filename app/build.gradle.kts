
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)

	id("com.google.devtools.ksp") version "2.3.2"
	id("com.google.dagger.hilt.android") version "2.58"
}

fun appVersionName(): String {
	val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.DDD"))
	return "a$date"
}

fun BaseAppModuleExtension.buildConfigFromLocalProperties(vararg keys: String) {
	val properties = Properties()
	val localPropertiesFile = project.rootProject.file("local.properties")
	if (localPropertiesFile.exists())
		localPropertiesFile.inputStream().use { properties.load(it) }

	keys.forEach { key ->
		val rawValue = properties.getProperty(key) ?: ""
		val (type, value) = when {
			rawValue.equals("true", true) || rawValue.equals("false", true) ->
				"boolean" to rawValue.lowercase()
			rawValue.toIntOrNull() != null -> "int" to rawValue
			else -> "String" to rawValue
		}
		defaultConfig.buildConfigField(type, key, value)
	}
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
		versionCode = 2
		versionName = appVersionName()

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		buildConfigFromLocalProperties("LM_STUDIO_URL", "WEBSITE_URL")
	}

	buildTypes {
		debug {
			applicationIdSuffix = ".dev"
			versionNameSuffix = "-dev"
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
	kotlin {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}
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

	implementation(libs.lifecycle.viewmodel.compose)
	implementation(libs.okhttp)
	implementation(libs.gson)
	implementation(libs.androidx.compose.animation)
	implementation(libs.androidx.navigation.testing)

	implementation("io.github.raamcosta.compose-destinations:core:2.3.0")
	ksp("io.github.raamcosta.compose-destinations:ksp:2.3.0")
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)
	implementation(libs.androidx.hilt.navigation.compose)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.tooling)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
}
