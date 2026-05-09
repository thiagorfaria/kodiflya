plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.ksp)
    compileOnly(libs.gradlePlugin.hilt)
}

gradlePlugin {
    plugins {
        register("kotlinJvm") {
            id = "kodiflya.kotlin.jvm"
            implementationClass = "KotlinJvmConventionPlugin"
        }
        register("androidLib") {
            id = "kodiflya.android.lib"
            implementationClass = "AndroidLibConventionPlugin"
        }
        register("androidComposeLib") {
            id = "kodiflya.android.compose.lib"
            implementationClass = "AndroidComposeLibConventionPlugin"
        }
        register("androidHilt") {
            id = "kodiflya.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
