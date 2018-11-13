package io.kesselring.blog

import kotlinx.html.*
import kotlin.reflect.KProperty

@DslMarker
annotation class BlogDsl

fun blog(initializer: Blog.() -> Unit): Blog = Blog(initializer)

@BlogDsl
class Blog(initializer: Blog.() -> Unit) {
    private val years: MutableMap<Int, Year> = mutableMapOf()
    var defaultAuthor: String? = null

    fun year(number: Int, block: Year.() -> Unit) {
        years[number] = Year(number, defaultAuthor).apply(block)
    }

    operator fun getValue(nothing: Nothing?, property: KProperty<*>): Blog {
        return this
    }

    init {
        initializer()
    }

    val asSection: BODY.() -> Unit = {
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
enum class M {
    January,
    February,
    March,
    April,
    May,
    June,
    July,
    August,
    September,
    October,
    November,
    December,
}

@BlogDsl
class Year(val number: Int, val defaultAuthor: String?) {
    val months: MutableMap<M, Month> = mutableMapOf()

    fun month(m: M, block: Month.() -> Unit) {
        months[m] = Month(defaultAuthor).apply(block)
    }

    val asContentDiv: DIV.() -> Unit = {
        months.forEach { (m, month) ->
            month.asContentDiv(this, m, number)
        }
    }
}

@BlogDsl
class Month(val defaultAuthor: String?) {
    private val entries: MutableList<Entry> = mutableListOf()
    fun entry(dayOfMonth: Int, block: Entry.() -> Unit) {
        entries.add(Entry(defaultAuthor, dayOfMonth).apply(block))
    }

    val asContentDiv: DIV.(m: M, y: Int) -> Unit = { m, y ->
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
class Entry(defaultAuthor: String?, val dayOfMonth: Int) {
    lateinit var title: String
    lateinit var subtitle: String
    var author: String? = defaultAuthor

    private val content: MutableList<Content> = mutableListOf()

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
}

sealed class Content {
    class Header(val text: String) : Content()
    class SubHeader(val text: String) : Content()
    class TextBlock(val text: String) : Content()
    class CodeBlock(val text: String) : Content()
    class Image(val link: String) : Content()
}
