package io.kesselring

import io.kesselring.blog.Blog
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.File
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

fun generateFiles(blog: Blog, buildDir: String) {
    val baseFolder = "$buildDir/web/"

    var pageCounter = AtomicInteger(0)

    val chunkedEntries = blog.years.flatMap { it.value.months.values }.flatMap { it.entries }
        .sortedByDescending { LocalDate.of(it.yearNumber, it.m, it.dayOfMonth) }
        .chunked(5)
    val navContents = chunkedEntries.map {
        val pageCount = pageCounter.incrementAndGet()
        val first = it.first().toLinkString()
        val last = it.last().toLinkString()
        val linkText = if (first == last) {
            "$first (#$pageCount)"
        } else {
            "$last - $first (#$pageCount)"
        }
        val result: DIV.() -> Unit = {
            a(classes = "bloglink", href = getFileName(pageCount) + "#left") { +linkText }
            br { }
        }
        result
    }

    pageCounter = AtomicInteger(0)
    chunkedEntries.forEach {
        val pageCount = pageCounter.incrementAndGet()
        val fileName = getFileName(pageCount)

        val targetFile = File("$baseFolder$fileName")
        println("targetFile=$targetFile")
        val file = targetFile.also {
            if (it.exists()) it.delete()
            it.createNewFile()
        }
        println("$file")
        file.apply {
            writer().apply {
                append(header)
                appendHTML(true, true).section("section") {
                    div("columns") {
                        div("column is-four-fifths") {
                            div("container") {
                                it.forEach {
                                    //println("${it.yearNumber} ${it.m} ${it.dayOfMonth} - ${it.title}")
                                }
                            }
                        }
                        div("column") {
                            navContents.forEach { it() }
                        }
                    }
                }
                append(footer)
            }.flush()
        }
        println(file.readText())
        println()
    }
}

private fun getFileName(pageCount: Int): String {
    val fileName = if (pageCount == 1) {
        "blog.html"
    } else {
        "blog-page-$pageCount.html"
    }
    return fileName
}