package com.mmoney.apps.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                defaultConfig.targetSdk = 35
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
            }
            
            configurations.all {
                resolutionStrategy {
                    force("com.squareup:javapoet:1.13.0")
                }
            }
        }
    }
}
