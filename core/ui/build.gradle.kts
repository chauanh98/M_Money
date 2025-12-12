plugins {
    id("mmoney.android.library")
    id("mmoney.android.library.compose")
}

android {
    namespace = "com.mmoney.apps.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    
    implementation(libs.androidx.compose.ui.tooling.preview)
}
