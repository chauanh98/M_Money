plugins {
    id("mmoney.android.library")
    id("mmoney.android.hilt")
}

android {
    namespace = "com.mmoney.apps.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.datastore.preferences)
}
