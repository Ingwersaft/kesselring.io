package io.kesselring

import io.kesselring.blog.Entry
import io.kesselring.blog.blog
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import java.time.Month
import java.util.concurrent.atomic.AtomicInteger

val randomId = AtomicInteger()
val blogContent by blog {
    defaultAuthor = "Marcel Kesselring"

    year(2017) {
        Month.values().forEach { m ->
            month(m) {
                repeat(3) {
                    entry(it + 1) {
                        title = "Something using someone (#${randomId.incrementAndGet()})"
                        subtitle = "Some random stuff you might find interesting: $it"
                        sukejuraContent()
                    }
                }
            }
        }
    }
    year(2018) {
        month(Month.NOVEMBER) {
            entry(dayOfMonth = 1) {
                title = "Scheduling using Sukejura"
                subtitle = "Getting started scheduling code-blocks on the JVM with Sukejura"
                sukejuraContent()
            }
        }
    }
}

private fun Entry.sukejuraContent() {
    header("Setup with Gradle")
    textBlock("First we have to add jcenter to our build file if not present yet:")
    codeBlock(
        """repositories {
        jcenter()
}"""
    )
    textBlock("Next add Sukejura dependency to our build file:")
    codeBlock("compile(\"io.kesselring.sukejura:Sukejura:<version>\")")
    textBlock("And finally, add you sukejura code, for example:")
    codeBlock(
        """    val sukejura = sukejura {
        schedule {
            // every working day of the week
            daysOfWeek {
                listOf(
                    DaysOfWeek.Mon,
                    DaysOfWeek.Tue,
                    DaysOfWeek.Wed,
                    DaysOfWeek.Thu,
                    DaysOfWeek.Fri
                )
            }
            // at 9am and 5pm
            hours {
                listOf(
                    Hours.H(9),
                    Hours.H(17)
                )
            }
            // every time the minute is 0
            minute { Minutes.M(0) }
            task {
                println("hello there!")
            }
        }
        start()
    }
    sukejura.schedules.first().invocations().take(20).forEach {
        println("triggering at: ${"$"}it")
    }"""
    )
}
