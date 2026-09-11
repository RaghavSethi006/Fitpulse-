import com.google.gms.googleservices.GoogleServicesPlugin.MissingGoogleServicesStrategy
import java.util.Base64

// Ensure .env has valid non-empty values so Secrets Gradle Plugin never generates invalid Java syntax in BuildConfig
val rootEnvFile = rootProject.file(".env")
if (rootEnvFile.exists()) {
  val envText = rootEnvFile.readText()
  val sanitizedText = envText.replace(Regex("""(?m)^GEMINI_API_KEY=\s*["']?\s*["']?\s*$"""), "GEMINI_API_KEY=MY_GEMINI_API_KEY")
  if (sanitizedText != envText) {
    rootEnvFile.writeText(sanitizedText)
  }
}

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.secrets)
  alias(libs.plugins.google.services)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.fitpulse.wqvpt"
    minSdk = 24
    targetSdk = 36
    val envVersionCode = System.getenv("APP_VERSION_CODE")?.toIntOrNull() ?: 1
    val envVersionName = System.getenv("APP_VERSION_NAME") ?: "1.0.0"

    versionCode = envVersionCode
    versionName = envVersionName

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  val debugKeystoreFile = file("${rootDir}/debug.keystore")
  if (!debugKeystoreFile.exists()) {
    val base64KeystoreFile = file("${rootDir}/debug.keystore.base64")
    if (base64KeystoreFile.exists()) {
      try {
        val cleanBase64 = base64KeystoreFile.readText().replace("\\s".toRegex(), "")
        val bytes = Base64.getDecoder().decode(cleanBase64)
        debugKeystoreFile.writeBytes(bytes)
      } catch (_: Exception) {}
    }
  }

  val rawKeystorePath = System.getenv("KEYSTORE_PATH")?.trim()?.takeIf { it.isNotEmpty() } ?: "${rootDir}/my-upload-key.jks"
  val releaseKeystoreFile = file(rawKeystorePath)
  val rawStorePassword = System.getenv("STORE_PASSWORD")?.trim()?.takeIf { it.isNotEmpty() }
  val isReleaseSigned = releaseKeystoreFile.exists() && rawStorePassword != null
  val defaultDebugKeystore = file("${rootDir}/debug.keystore")
  val userHomeDebugKeystore = file("${System.getProperty("user.home")}/.android/debug.keystore")
  val activeDebugKeystore = when {
    defaultDebugKeystore.exists() -> defaultDebugKeystore
    userHomeDebugKeystore.exists() -> userHomeDebugKeystore
    else -> defaultDebugKeystore
  }
  val hasDebugKeystore = defaultDebugKeystore.exists() || userHomeDebugKeystore.exists()

  signingConfigs {
    create("release") {
      storeFile = if (isReleaseSigned) releaseKeystoreFile else activeDebugKeystore
      storePassword = rawStorePassword ?: "android"
      keyAlias = System.getenv("KEY_ALIAS")?.trim()?.takeIf { it.isNotEmpty() } ?: (if (isReleaseSigned) "upload" else "androiddebugkey")
      keyPassword = System.getenv("KEY_PASSWORD")?.trim()?.takeIf { it.isNotEmpty() } ?: (rawStorePassword ?: "android")
    }
    create("debugConfig") {
      storeFile = activeDebugKeystore
      storePassword = "android"
      keyAlias = "androiddebugkey"
      keyPassword = "android"
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = when {
        isReleaseSigned -> signingConfigs.getByName("release")
        hasDebugKeystore -> signingConfigs.getByName("debugConfig")
        else -> null
      }
    }
    debug {
      signingConfig = if (hasDebugKeystore) signingConfigs.getByName("debugConfig") else null
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
  testOptions {
    unitTests {
      isIncludeAndroidResources = false
      isReturnDefaultValues = true
      all {
        it.jvmArgs(
          "-Djava.awt.headless=true",
          "-Dfile.encoding=UTF-8",
          "-Dsun.java2d.opengl=false",
          "-Dsun.java2d.d3d=false"
        )
        it.systemProperty("java.awt.headless", "true")
      }
    }
  }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Configure the Secrets Gradle Plugin to use .env and .env.example files
// to match the convention used in Web projects.
secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
  ignoreList.add("FIREBASE_APPCHECK_DEBUG_TOKEN")
}

googleServices { missingGoogleServicesStrategy = MissingGoogleServicesStrategy.WARN }

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(platform(libs.firebase.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  // implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.converter.moshi)
  implementation(libs.firebase.ai)
  // Uncomment to use Firestore:
  // implementation(libs.firebase.firestore)

  // Uncomment ALL FOUR of the following dependencies together to use Firebase Auth and Google
  // Sign-In via Credential Manager:
  // implementation(libs.firebase.auth)
  // implementation(libs.androidx.credentials)
  // implementation(libs.androidx.credentials.play.services)
  // implementation(libs.googleid)
  implementation(libs.firebase.appcheck.recaptcha)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp)
  // implementation(libs.play.services.location)
  implementation(libs.retrofit)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
  "ksp"(libs.moshi.kotlin.codegen)
}
