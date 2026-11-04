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

extensions.extraProperties["ARTIFACT_ID"] = "kherkin-espresso"
extensions.extraProperties["DESCRIPTION"] = "An Android UI testing framework for XML layouts that simplifies writing UI tests"

// Must match the values SharedPublishPlugin (kherkin.publish) sets for all library modules.
configure<LibraryExtension> {
    namespace = "com.progressive.kherkin.espresso"

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
    implementation(libs.androidx.espresso.device)
    api(libs.androidx.uiautomator)
    api(libs.espresso.core)
    api(libs.espresso.contrib)
    api(project(":common"))
}