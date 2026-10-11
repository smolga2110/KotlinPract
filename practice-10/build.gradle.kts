plugins { id("com.android.application"); kotlin("android"); kotlin("plugin.compose") }
android {
 namespace="ru.mazenin.kotlin"
 compileSdk=35
 defaultConfig { applicationId="ru.mazenin.kotlin.pr10"; minSdk=26; targetSdk=35; versionCode=1; versionName="1.0"; testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner" }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
 buildFeatures { compose=true }
}
dependencies {
implementation("androidx.core:core-ktx:1.16.0")
implementation("androidx.appcompat:appcompat:1.7.1")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
testImplementation("junit:junit:4.13.2")
implementation(platform("androidx.compose:compose-bom:2025.04.01"))
implementation("androidx.activity:activity-compose:1.10.1")
implementation("androidx.compose.material3:material3")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.0")
}

dependencies { implementation("androidx.compose.runtime:runtime-livedata") }
