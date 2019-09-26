fun main(args: Array<String>) {
    val bucketName = "kesselring.io"
    val prefix = "bierschmidts/"
    s3.listObjects(bucketName, prefix).objectSummaries
        .filter { it.key.contains(Regex("\\s+")) }
        .forEach {
            println(it.key)
            s3.copyObject(bucketName, it.key, bucketName, it.key.replace(Regex("\\s+"), "_"))
            s3.deleteObject(bucketName, it.key)
        }
}