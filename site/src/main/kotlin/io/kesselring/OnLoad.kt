import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.dom.removeClass

private val closables by lazy { document.querySelectorAll(".closable").asList() }
private val showables by lazy { document.querySelectorAll(".showables").asList() }
fun main(args: Array<String>) {
    window.onload = {
        println("hello there")

        handleNavbarStickinessDesktop()
        addedNavbarBurberClickFunctionality()

        showables.map { it as Element }.forEach {
            it.addEventListener("click", callback = {
                println("showables clicked")
                showClosables()
                hide("impressum")
                hide("datenschutzerklaerung")
            })
        }

        addImpressumShowHideBehaviour()
        addDatenschutzShowHideBehaviour()
    }
}

private fun addImpressumShowHideBehaviour() {
    document.getElementById("impressumLink")?.addEventListener("click", callback = {
        println("impressumLink clicked")
        closables.forEach {
            it as Element
            it.setAttribute("style", "display: none;")
        }
        show("impressum")
        hide("datenschutzerklaerung")
    })
}

private fun addDatenschutzShowHideBehaviour() {
    document.getElementById("datenschutzerklaerungLink")?.addEventListener("click", callback = {
        println("datenschutzerklaerungLink clicked")
        closables.forEach {
            it as Element
            it.setAttribute("style", "display: none;")
        }
        hide("impressum")
        show("datenschutzerklaerung")
    })
}

private fun showClosables() = closables.forEach {
    it as Element
    it.removeAttribute("style")
}

private fun show(id: String) {
    document.getElementById(id)?.let {
        it.removeAttribute("style")
    }
}

private fun hide(id: String) {
    document.getElementById(id)?.let {
        it.setAttribute("style", "display: none;")
    }
}

private fun addedNavbarBurberClickFunctionality() {
    val burgerNodes = document.querySelectorAll(".navbar-burger").asList()
    burgerNodes.forEach { node: Node ->
        node.addEventListener("click", callback = {
            node as Element
            val target = node.getAttribute("data-target")?.let {
                document.getElementById(it)
            }
            node.classList.toggle("is-active")
            target?.classList?.toggle("is-active")
        })
    }
    document.getElementsByClassName("navbar-item").asList().forEach {
        it.addEventListener("click", callback = {
            burgerNodes.map { it as Element }.forEach {
                it.removeClass("is-active")
                val target = it.getAttribute("data-target")?.let {
                    document.getElementById(it)
                }
                target?.removeClass("is-active")
            }
        })
    }
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