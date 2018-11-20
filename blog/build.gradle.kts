plugins {
    kotlin("jvm")
    application
}
application {
    mainClassName = "io.kesselring.MainKt"
    applicationDefaultJvmArgs = listOf("-Dbuild.dir=${project.buildDir}")
}
dependencies {
    compile(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.11")
}
tasks {
    //    val preparation by creating {
//        outputs.upToDateWhen { false }
//        mkdir("$buildDir/web")
//    }
    findByName("run")?.let {
        it.doFirst {
            mkdir("$buildDir/web")
        }
        it.outputs?.upToDateWhen { false }
    }
}
//tasks.findByName("run")?.dependsOn("preparation")
tasks.findByName("build")?.dependsOn("run")