import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.HostTestBuilder
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.compose.compiler)
}

configure<ApplicationExtension> {
    namespace = "com.progressive.kherkin.sampleapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.progressive.kherkin.sampleapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests {
            isIncludeAndroidResources = true
        }
        managedDevices {
            localDevices {
                create("pixel5api35") {
                    device = "Pixel 5"
                    apiLevel = 35
                    systemImageSource = "aosp-atd"
                    testedAbi = "x86_64"
                }
            }
        }
        emulatorControl {
            enable = true
        }
    }
}

configure<ApplicationAndroidComponentsExtension> {
    beforeVariants {
        it.hostTests[HostTestBuilder.UNIT_TEST_TYPE]?.enable = true
    }
}

extensions.configure<KotlinAndroidProjectExtension> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.tooling.preview)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(project(":common"))
    androidTestImplementation(project(":compose"))
    androidTestImplementation(project(":espresso"))
    androidTestUtil(libs.androidx.orchestrator)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    compileOnly(libs.viewbinding)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.manifest)
}