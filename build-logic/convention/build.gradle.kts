import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.progressive.kherkin.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.vanniktech.maven.publish)
}

val javaTarget = libs.versions.jvmTarget.get()

java {
    sourceCompatibility = JavaVersion.toVersion(javaTarget)
    targetCompatibility = JavaVersion.toVersion(javaTarget)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(javaTarget)
    }
}

gradlePlugin {
    plugins {
        register("sharedPublish") {
            id = libs.plugins.kherkin.publish.get().pluginId
            implementationClass = "SharedPublishPlugin"
        }
    }
}