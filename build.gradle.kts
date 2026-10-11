plugins {
 id("com.android.application") version "8.13.2" apply false
 kotlin("android") version "2.2.21" apply false
 kotlin("plugin.compose") version "2.2.21" apply false
}
// ASCII build paths avoid Windows tool failures in Cyrillic directories.
subprojects { layout.buildDirectory.set(file(System.getenv("KOTLIN_BUILD_DIR") ?: (System.getProperty("java.io.tmpdir") + "/KotlinPracticesGitHub" )).resolve(name)) }
