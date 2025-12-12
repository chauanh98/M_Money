plugins {
    id("mmoney.android.library")
    id("mmoney.android.room")
    id("mmoney.android.hilt")
}

android {
    namespace = "com.mmoney.apps.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
