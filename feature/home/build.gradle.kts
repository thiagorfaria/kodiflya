plugins {
    id("kodiflya.android.compose.lib")
    id("kodiflya.android.hilt")
}

android {
    namespace = "com.kodiflya.feature.home"
}

dependencies {
    implementation(project(":core:plugin"))
    implementation(project(":ui:component"))
    implementation(project(":ui:theme"))
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
}
