plugins {
    id("kodiflya.android.compose.lib")
    id("kodiflya.android.hilt")
}

android {
    namespace = "com.kodiflya.feature.search"
}

dependencies {
    implementation(project(":core:plugin"))
    implementation(project(":core:engine"))
    implementation(project(":ui:algorithm"))
    implementation(project(":ui:component"))
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
}
