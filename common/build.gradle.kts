import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kherkin.publish)
}

extra.set("ARTIFACT_ID", "kherkin-common")
extra.set("DESCRIPTION", "A dependency for kherkin-espresso and kherkin-compose")

// Matches the values set centrally by the kherkin.publish convention plugin (SharedPublishPlugin).
configure<LibraryExtension> {
    namespace = "com.progressive.kherkin.common"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

configure<KotlinAndroidProjectExtension> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_4)
        apiVersion.set(KotlinVersion.KOTLIN_2_4)
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.runner)
    implementation(libs.findbugs)
    implementation(libs.javax.inject)
    api(libs.kotlin.test)
    api(libs.androidx.rules)
    api(libs.kotlin.reflect)
}