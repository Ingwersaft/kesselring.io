package io.kesselring

import io.kesselring.PaginatedOverview.blogNavColumn
import io.kesselring.blog.Blog
import io.kesselring.blog.Content
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.io.File
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object SingleBlogEntries {
    fun generateFiles(blog: Blog, baseFolder: String) {
        val (chunkedEntries, navContents) = PaginatedOverview.generateChunksAndNav(blog)

        blog.years.flatMap { it.value.months.values }.flatMap { it.entries }.forEach { entry ->
            val filename = entry.titleFileName()
            File("$baseFolder$filename").run {
                if (exists()) delete()
                createNewFile()
                writer().apply {
                    append(header)
                    appendHTML(true, true).section("section") {
                        div("columns") {
                            div("column is-four-fifths") {
                                div("box") {
                                    h1("title") {
                                        a(classes = "link") { +entry.title }
                                    }
                                    h5 {
                                        val year = entry.yearNumber
                                        val month = entry.m.getDisplayName(
                                            TextStyle.FULL,
                                            Locale.ENGLISH
                                        )
                                        val day = entry.dayOfMonth.toString().padStart(
                                            2,
                                            '0'
                                        )
                                        +"published on the $day $month of $year by ${entry.author}"

                                    }
                                    h2("subtitle is-4") {
                                        +entry.subtitle
                                    }
                                    //<progress class="progress is-danger" value="90" max="100">90%</progress>
                                    progress("progress is-small is-danger") {
                                        value = "100"
                                        max = "100"
                                        +"100%"
                                    }
                                    entry.content.forEach { content ->
                                        when (content) {
                                            is Content.Header -> {
                                                h4("title is-4") { +content.text }
                                            }
                                            is Content.SubHeader -> {
                                                //<h6 class="subtitle is-6">Subtitle 6</h6>
                                                h6("title is-5") { +content.text }
                                            }
                                            is Content.TextBlock -> {
                                                p("") { +content.text }
                                            }
                                            is Content.CodeBlock -> {
                                                div("notification") {
                                                    pre {
                                                        code {
                                                            +content.text
                                                        }
                                                    }
                                                }
                                            }
                                            is Content.Image -> {
                                                // TODO
                                            }
                                        }
                                    }
                                }

                            }
                            blogNavColumn(navContents)
                        }
                    }

                    append(footer)
                }.flush()
            }
        }
    }
}

fun main(args: Array<String>) {
    SingleBlogEntries.generateFiles(blogContent, "C:/Users/CUBE/IdeaProjects/kesselring.io/blog/build/web/")
}
