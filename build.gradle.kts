import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    base
    kotlin("jvm") apply false
}
allprojects {
    repositories {
        jcenter()
    }
}