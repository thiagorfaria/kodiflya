plugins {
    id("kodiflya.android.lib")
    id("kodiflya.android.hilt")
}

android {
    namespace = "com.kodiflya.core.engine"
}

dependencies {
    implementation(project(":core:plugin"))
    implementation(libs.coroutines.android)
}
