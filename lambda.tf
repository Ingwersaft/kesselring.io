resource "aws_s3_bucket" "deployment" {
  bucket = "${var.site-without-dots}-deployments"
  acl = "private"
  tags {
    "type" = "${var.tag}"
  }
}
resource "aws_s3_bucket_object" "zip" {
  bucket = "${aws_s3_bucket.deployment.bucket}"
  key = "lambda.zip"
  source = "lambda/build/distributions/lambda.zip"
  etag = "${md5(file("lambda/build/distributions/lambda.zip"))}"
  tags {
    "type" = "${var.tag}"
  }
}
resource "aws_lambda_function" "invalidate" {
  s3_bucket = "${aws_s3_bucket.deployment.bucket}"
  s3_key = "${aws_s3_bucket_object.zip.key}"
  function_name = "invalidate-${var.site-without-dots}-distribution"
  role = "${aws_iam_role.iam_for_lambda.arn}"
  handler = "kesselring.io.InvalidateLambda::handleRequest"
  source_code_hash = "${base64sha256(file("lambda/build/distributions/lambda.zip"))}"
  runtime = "java8"
  timeout = "20"
  memory_size = "256"
  environment {
    variables {
      distribution = "${aws_cloudfront_distribution.cloudfront.id}"
      queue = "${aws_sqs_queue.s3_changed_queue.name}"
    }
  }
  tags {
    "type" = "${var.tag}"
  }
}