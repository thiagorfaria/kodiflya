plugins {
    id("kodiflya.android.compose.lib")
}

android {
    namespace = "com.kodiflya.ui.component"
}

dependencies {
    implementation(project(":core:plugin"))
    implementation(project(":ui:theme"))
}
