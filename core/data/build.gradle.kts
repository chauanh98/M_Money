plugins {
    id("mmoney.android.library")
    id("mmoney.android.hilt")
}

android {
    namespace = "com.mmoney.apps.core.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
}
