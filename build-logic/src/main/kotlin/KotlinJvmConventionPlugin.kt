import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class KotlinJvmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension>("kotlin") {
                jvmToolchain(17)
            }

            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }

            val libs = versionCatalog()
            dependencies {
                "testImplementation"(libs.lib("junit-jupiter-api"))
                "testRuntimeOnly"(libs.lib("junit-jupiter-engine"))
                "testRuntimeOnly"(libs.lib("junit-platform-launcher"))
                "testImplementation"(libs.lib("mockk"))
                "testImplementation"(libs.lib("coroutines-test"))
            }
        }
    }
}

internal fun Project.versionCatalog(): VersionCatalog =
    extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.lib(alias: String) = findLibrary(alias).get()
