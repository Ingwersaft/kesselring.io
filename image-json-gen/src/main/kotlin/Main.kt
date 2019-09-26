import com.amazonaws.regions.Regions
import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder
import com.amazonaws.services.cloudfront.model.CreateInvalidationRequest
import com.amazonaws.services.cloudfront.model.InvalidationBatch
import com.amazonaws.services.cloudfront.model.Paths
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.time.LocalDateTime

val s3 by lazy {
    AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build()
}
val jackson by lazy {
    ObjectMapper()
}
val cloudfront by lazy {
    AmazonCloudFrontClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build()
}

fun main(args: Array<String>) {
    val bucketName = "kesselring.io"
    val prefix = "bierschmidts/"
    val targetName = "keys.json"
    val keys = s3.listObjects(bucketName, prefix).objectSummaries
        .filterNot { it.key == prefix }
        .filterNot { it.key.endsWith(targetName) }
        .map {
            it.key
        }

    s3.putObject(bucketName, "$prefix$targetName", jackson.writeValueAsString(keys))
    println("written keys.json with ${keys.size} entries")

    val distId = File("${System.getProperty("user.home")}${File.separator}.kesselring-io-dist.txt").readText().trim()
    println("distId=$distId")
    cloudfront.createInvalidation(
        CreateInvalidationRequest(
            distId,
            InvalidationBatch(Paths().withItems("/$prefix$targetName").withQuantity(1), LocalDateTime.now().toString())
        )
    ).let {
        println("result= $it")
    }
}