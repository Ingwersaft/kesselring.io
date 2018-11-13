package io.kesselring

fun main(args: Array<String>) {
    println("hi")
    val buildDir = System.getProperty("build.dir") ?: ""
    println("buildDir=$buildDir")

    generateFiles(blogContent,buildDir)
}