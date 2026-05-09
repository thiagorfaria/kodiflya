import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kodiflya.android.lib")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                buildFeatures { compose = true }
            }

            val libs = versionCatalog()
            dependencies {
                val bom = platform(libs.lib("compose-bom"))
                "implementation"(bom)
                "implementation"(libs.lib("compose-ui"))
                "implementation"(libs.lib("compose-ui-graphics"))
                "implementation"(libs.lib("compose-ui-tooling-preview"))
                "implementation"(libs.lib("compose-material3"))
                "implementation"(libs.lib("compose-material-icons-extended"))
                "debugImplementation"(libs.lib("compose-ui-tooling"))
                "androidTestImplementation"(bom)
                "androidTestImplementation"(libs.lib("compose-ui-test-junit4"))
                "androidTestImplementation"(libs.lib("androidx-test-ext-junit"))
                "debugImplementation"(libs.lib("compose-ui-test-manifest"))
            }
        }
    }
}
