plugins {
    kotlin("jvm")
}
dependencies {
    val aws_version: String by project
    val lambda_version: String by project
    compile(kotlin("stdlib-jdk8"))
    compile("com.amazonaws:aws-java-sdk-sqs:$aws_version")
    compile("com.amazonaws:aws-java-sdk-cloudfront:$aws_version")
    compile("com.amazonaws:aws-lambda-java-core:$lambda_version")
}
tasks {
    val buildZip by creating(Zip::class) {
        from("${project.buildDir}/classes/kotlin/main")
        into("lib") {
            from(configurations.compileClasspath)
        }
    }
    val build by existing
    build.get().dependsOn(buildZip)
}
