import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.mmoney.apps.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.room.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.javapoet)
}

configurations.all {
    resolutionStrategy {
        force(libs.javapoet)
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mmoney.android.application"
            implementationClass = "com.mmoney.apps.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "mmoney.android.library"
            implementationClass = "com.mmoney.apps.convention.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "mmoney.android.library.compose"
            implementationClass = "com.mmoney.apps.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "mmoney.android.feature"
            implementationClass = "com.mmoney.apps.convention.AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "mmoney.android.hilt"
            implementationClass = "com.mmoney.apps.convention.AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "mmoney.android.room"
            implementationClass = "com.mmoney.apps.convention.AndroidRoomConventionPlugin"
        }
    }
}
