package io.kesselring.blog

import kotlinx.html.*
import java.time.Month
import java.time.format.TextStyle
import java.util.*
import kotlin.reflect.KProperty

@DslMarker
annotation class BlogDsl

fun blog(initializer: Blog.() -> Unit): Blog = Blog(initializer)

@BlogDsl
class Blog(initializer: Blog.() -> Unit) {
    val years: MutableMap<Int, Year> = mutableMapOf()
    var defaultAuthor: String? = null

    fun year(yearNumber: Int, block: Year.() -> Unit) {
        years[yearNumber] = Year(yearNumber, defaultAuthor).apply(block)
    }

    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Blog {
        return this
    }

    init {
        initializer()
    }

    val mainPage: BODY.() -> Unit = {
        //<!-- blog -->
        //<section id="blogSection" class="section">
        //    <div class="container">
        //        <div class="columns">
        //            <div id="blogContent" class="column is-four-fifths">
        //                <h1>stuff</h1>
        //            </div>
        //            <div id="blogSidebar" class="column">
        //                <h1>stuff</h1>
        //            </div>
        //        </div>
        //    </div>
        //</section>

        section("section") {
            div("container") {
                div("columns") {
                    div("column is-four-fifths") {
                        val currentYear = years.entries.sortedByDescending { it.key }.first().value
                        currentYear.asContentDiv(this)
                    }
                    div("column") {
                        generateSideNav()
                    }
                }
            }
        }
    }

    fun DIV.generateSideNav() {
    }
}

@BlogDsl
class Year(val yearNumber: Int, val defaultAuthor: String?) {
    val months: MutableMap<Month, MonthObj> = mutableMapOf()

    fun month(m: Month, block: MonthObj.() -> Unit) {
        months[m] = MonthObj(yearNumber, m, defaultAuthor).apply(block)
    }

    val asContentDiv: DIV.() -> Unit = {
        months.forEach { (m, month) ->
            month.asContentDiv(this, m, yearNumber)
        }
    }
}

@BlogDsl
class MonthObj(val yearNumber: Int, val m: Month, val defaultAuthor: String?) {
    val entries: MutableList<Entry> = mutableListOf()
    fun entry(dayOfMonth: Int, block: Entry.() -> Unit) {
        entries.add(Entry(yearNumber, m, defaultAuthor, dayOfMonth).apply(block))
    }

    val asContentDiv: DIV.(m: Month, y: Int) -> Unit = { m, y ->
        entries.forEach {
            h1 {
                +it.title
            }
            h3 { +"Posted on ${m.name} ${it.dayOfMonth}, $y by ${it.author}" }
            p {
                +it.subtitle
            }
        }
    }
}

@BlogDsl
class Entry(val yearNumber: Int, val m: Month, defaultAuthor: String?, val dayOfMonth: Int) {
    lateinit var title: String
    lateinit var subtitle: String
    var author: String? = defaultAuthor

    val content: MutableList<Content> = mutableListOf()

    fun header(text: String) {
        content.add(Content.Header(text))
    }

    fun subHeader(text: String) {
        content.add(Content.SubHeader(text))
    }

    fun textBlock(text: String) {
        content.add(Content.TextBlock(text))
    }

    fun codeBlock(text: String) {
        content.add(Content.CodeBlock(text))
    }

    fun image(link: String) {
        content.add(Content.Image(link))
    }

    fun toLinkString(): String = "$yearNumber-${m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}"

    fun titleFileName() = "blog-" + (title.toLowerCase()
        .replace(' ', '-')
        .replace("#", "")
        .replaceAll(listOf('*', '.', '"', '/', '\\', '[', ']', ':', ';', '|', '=', ',', '(', ')'), "") + ".html")
}

private tailrec fun String.replaceAll(toBeReplaced: List<Char>, replacement: String): String = when {
    toBeReplaced.isEmpty() -> this
    toBeReplaced.size > 1 -> replace(toBeReplaced.first().toString(), replacement).replaceAll(
        toBeReplaced.subList(1, toBeReplaced.size),
        replacement
    )
    else -> replace(toBeReplaced.first().toString(), replacement)
}

sealed class Content {
    class Header(val text: String) : Content()
    class SubHeader(val text: String) : Content()
    class TextBlock(val text: String) : Content()
    class CodeBlock(val text: String) : Content()
    class Image(val link: String) : Content()
}
