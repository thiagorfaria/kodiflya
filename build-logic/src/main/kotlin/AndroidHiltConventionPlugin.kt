import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            val libs = versionCatalog()
            val hasCompose = pluginManager.hasPlugin("org.jetbrains.kotlin.plugin.compose")
            dependencies {
                "implementation"(libs.lib("hilt-android"))
                "ksp"(libs.lib("hilt-compiler"))
                if (hasCompose) {
                    "implementation"(libs.lib("hilt-navigation-compose"))
                }
            }
        }
    }
}
