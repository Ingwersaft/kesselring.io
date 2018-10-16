import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.dom.removeClass

lateinit var scrollPosition: String
fun main(args: Array<String>) {
    window.onload = {
        println("hello there")

        handleNavbarStickinessDesktop()
        addedNavbarBurberClickFunctionality()
    }
}

private fun addedNavbarBurberClickFunctionality() {
    document.querySelectorAll(".navbar-burger").asList().forEach { node: Node ->
        node.addEventListener("click", callback = {
            node as Element
            val target = node.getAttribute("data-target")?.let {
                document.getElementById(it)
            }
            node.classList.toggle("is-active")
            target?.classList?.toggle("is-active")
        })
    }
//    document.getElementsByClassName("navbar-burger").asList().firstOrNull()?.let { element ->
//        println("addint onclick event listener")
//        element.addEventListener("click", callback = {
//            println("navbar-burger clicked!")
//        })
//    }
}

private fun handleNavbarStickinessDesktop() {
    window.onscroll = {
        document.getElementsByClassName("navbar").asList().firstOrNull()?.let {
            val navTopPosition = it.getBoundingClientRect().top
            if (navTopPosition > 0) {
                it.removeClass("is-fixed-top")
            } else {
                it.addClass("is-fixed-top")
            }
        }
    }
}