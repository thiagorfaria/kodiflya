plugins {
    id("kodiflya.android.compose.lib")
}

android {
    namespace = "com.kodiflya.feature.splash"
}

dependencies {
    implementation(project(":ui:component"))
    implementation(project(":ui:theme"))
}
