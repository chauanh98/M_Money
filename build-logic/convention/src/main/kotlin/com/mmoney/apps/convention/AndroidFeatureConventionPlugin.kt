package com.mmoney.apps.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("mmoney.android.library")
            pluginManager.apply("mmoney.android.hilt")
            
            extensions.configure<LibraryExtension> {
                
                // defaultRunner = "com.mmoney.apps.core.testing.MMoneyTestRunner" // To be implemented
            }

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:common"))
                
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
            }
        }
    }
}
