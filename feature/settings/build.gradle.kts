plugins {
    id("mmoney.android.feature")
    id("mmoney.android.library.compose")
}

android {
    namespace = "com.mmoney.apps.feature.settings"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
}
