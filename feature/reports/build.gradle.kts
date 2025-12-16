plugins {
    id("mmoney.android.feature")
    id("mmoney.android.library.compose")
}

android {
    namespace = "com.mmoney.apps.feature.reports"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
}
