package io.kesselring

import java.lang.IllegalStateException

fun main(args: Array<String>) {
    println("hi")
    val buildDir = System.getProperty("build.dir") ?: ""
    println("buildDir=$buildDir")

    val titleDublicates =
        blogContent.years.flatMap { it.value.months.values }.flatMap { it.entries }.map { it.titleFileName() }
            .groupBy { it }
            .filter { it.value.size > 1 }
    if (titleDublicates.isNotEmpty()) {
        val warnText = "found title filename duplicates!"
        println(warnText)
        titleDublicates.keys.forEach(::println)
        throw IllegalStateException(warnText)
    }
    PaginatedOverview.generateFiles(blogContent, "$buildDir/web/")
    SingleBlogEntries.generateFiles(blogContent, "$buildDir/web/")
}