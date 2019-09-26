import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper

val s3 by lazy {
    AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).build()
}
val jackson by lazy {
    ObjectMapper()
}

fun main(args: Array<String>) {
    val bucketName = "kesselring.io"
    val prefix = "bierschmidts/"
    val keys = s3.listObjects(bucketName, prefix).objectSummaries
        .filterNot { it.key == prefix }
        .map {
            it.key
        }

    s3.putObject(bucketName, "${prefix}keys.json", jackson.writeValueAsString(keys))
}