plugins {
    id("mmoney.android.library")
    id("mmoney.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mmoney.apps.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
}
