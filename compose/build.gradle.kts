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

extensions.extraProperties["ARTIFACT_ID"] = "kherkin-compose"
extensions.extraProperties["DESCRIPTION"] = "An Android UI testing framework for Jetpack Compose screens that simplifies writing UI tests"

// Must match the values set centrally by kherkin.publish (SharedPublishPlugin).
configure<LibraryExtension> {
    namespace = "com.progressive.kherkin.compose"

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
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.javax.inject)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    api(libs.compose.test)
    api(libs.compose.testing)
    api(project(":common"))
}