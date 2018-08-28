data "archive_file" "lambda_zip" {
  type = "zip"
  source_file = "invalidate.py"
  output_path = "invalidate.zip"
}

resource "aws_lambda_function" "func" {
  filename = "${data.archive_file.lambda_zip.output_path}"
  function_name = "invalidate_${var.site-without-dots}"
  role = "${aws_iam_role.iam_for_lambda.arn}"
  handler = "invalidate.handler"
  source_code_hash = "${base64sha256(file(data.archive_file.lambda_zip.output_path))}"
  runtime = "python2.7"
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

