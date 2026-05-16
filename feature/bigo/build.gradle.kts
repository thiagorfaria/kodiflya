plugins {
    id("kodiflya.android.compose.lib")
}

android {
    namespace = "com.kodiflya.feature.bigo"
}

dependencies {
    implementation(project(":core:plugin"))
    implementation(project(":ui:theme"))
    implementation(project(":ui:component"))
}
