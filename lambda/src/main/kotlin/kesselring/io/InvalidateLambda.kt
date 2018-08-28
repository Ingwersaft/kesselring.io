package kesselring.io

import com.amazonaws.services.cloudfront.AmazonCloudFrontClientBuilder
import com.amazonaws.services.cloudfront.model.CreateInvalidationRequest
import com.amazonaws.services.cloudfront.model.InvalidationBatch
import com.amazonaws.services.cloudfront.model.ListInvalidationsRequest
import com.amazonaws.services.cloudfront.model.Paths
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry
import com.amazonaws.services.sqs.model.Message
import java.time.LocalDateTime
import java.util.*

val cloudFront = AmazonCloudFrontClientBuilder.defaultClient()
val sqs = AmazonSQSClientBuilder.defaultClient()

class InvalidateLambda : RequestHandler<String, String> {
    override fun handleRequest(input: String?, context: Context?): String {
        try {
            println("handleRequest: $input")
            val queueUrl = sqs.getQueueUrl(System.getenv("queue")).queueUrl

            val allMessages: MutableList<Message> = mutableListOf()
            var lastBatch: MutableList<Message>
            do {
                lastBatch = sqs.receiveMessage(queueUrl).messages
                allMessages.addAll(lastBatch)
            } while (lastBatch.size != 0)
            println("allMessages=${allMessages.size}")
            if (allMessages.size == 0) {
                return logAndReturn("no s3 update messages, so nothing to invalidate")
            }
            val deleteMessageBatch = sqs.deleteMessageBatch(
                queueUrl,
                allMessages.map { DeleteMessageBatchRequestEntry(UUID.randomUUID().toString(), it.receiptHandle) })
            println("deleted: $deleteMessageBatch")
            //
            val distributionId = System.getenv("distribution")
            val anyInProgress =
                cloudFront.listInvalidations(ListInvalidationsRequest(distributionId).withMaxItems("50"))
                    .let {
                        it.invalidationList.items.any { it.status == "InProgress" }
                    }
            println("anyInProgress=$anyInProgress")
            if (anyInProgress) {
                return logAndReturn("skipping invalidation")
            }

            cloudFront.createInvalidation(
                CreateInvalidationRequest(
                    distributionId,
                    InvalidationBatch(Paths().withItems("/*").withQuantity(1), LocalDateTime.now().toString())
                )
            ).let {
                println("result= $it")
            }
            return logAndReturn("invalidated")
        } catch (e: Exception) {
            println("error: ${e.message}")
            return "error: ${e.message}"
        }
    }
}

fun logAndReturn(s: String): String {
    println(s)
    return s
}

fun main(args: Array<String>) {
    println(60*24*30)
}