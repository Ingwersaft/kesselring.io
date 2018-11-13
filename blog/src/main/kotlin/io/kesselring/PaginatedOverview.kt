package io.kesselring

import io.kesselring.blog.Blog
import io.kesselring.blog.Entry
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.File
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object PaginatedOverview {
    fun generateFiles(blog: Blog, baseFolder: String) {
        val (chunkedEntries, navContents) = generateChunksAndNav(blog)

        val pageCounter2 = AtomicInteger(0)
        chunkedEntries.forEach {
            val pageCount = pageCounter2.incrementAndGet()
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
                                it.forEach {
                                    div("box") {
                                        h1("title") {
                                            a(href = "${it.titleFileName()}#left", classes = "link") { +it.title }
                                        }
                                        h5 {
                                            val year = it.yearNumber
                                            val month = it.m.getDisplayName(
                                                TextStyle.FULL,
                                                Locale.ENGLISH
                                            )
                                            val day = it.dayOfMonth.toString().padStart(
                                                2,
                                                '0'
                                            )
                                            +"published on the $day $month of $year by ${it.author}"

                                        }
                                        h2("subtitle is-4") {
                                            +it.subtitle
                                        }
                                        //println("${it.yearNumber} ${it.m} ${it.dayOfMonth} - ${it.title}")
                                    }
                                }
                            }
                            blogNavColumn(navContents)
                        }
                    }

                    append(footer)
                }.flush()
            }
            println(file.readText())
            println()
        }
    }

    internal fun DIV.blogNavColumn(navContents: List<DIV.() -> Unit>) {
        div("column notification") {
            h2("subtitle") {
                +"Navigation"
            }
            navContents.forEach { it() }
        }
    }

    internal fun generateChunksAndNav(blog: Blog): Pair<List<List<Entry>>, List<DIV.() -> Unit>> {
        val pageCounter = AtomicInteger(0)
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
        return Pair(chunkedEntries, navContents)
    }

    private fun getFileName(pageCount: Int): String {
        val fileName = if (pageCount == 1) {
            "blog.html"
        } else {
            "blog-page-$pageCount.html"
        }
        return fileName
    }
}