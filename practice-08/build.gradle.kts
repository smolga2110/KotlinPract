plugins { id("com.android.application"); kotlin("android");  }
android {
 namespace="ru.mazenin.kotlin"
 compileSdk=35
 defaultConfig { applicationId="ru.mazenin.kotlin.pr08"; minSdk=26; targetSdk=35; versionCode=1; versionName="1.0"; testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner" }
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
 kotlinOptions { jvmTarget="17" }
 
}
dependencies {
implementation("androidx.core:core-ktx:1.16.0")
implementation("androidx.appcompat:appcompat:1.7.1")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test:runner:1.6.2")
androidTestImplementation("androidx.test:rules:1.6.1")
androidTestImplementation("androidx.test.ext:junit:1.2.1")
androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
