plugins {
    kotlin("jvm")
}
dependencies {
    val s3_version: String by project
    compile(kotlin("stdlib-jdk8"))
    compile("com.amazonaws:aws-java-sdk-s3:$s3_version")
}
