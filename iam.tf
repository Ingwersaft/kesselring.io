resource "aws_iam_role" "iam_for_lambda" {
  name = "iam-for-${var.site-without-dots}-lambda"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow"
    }
  ]
}
EOF
}
resource "aws_iam_policy" "lambdapolicy" {
  name = "lambda-policy-${var.site-without-dots}"
  description = "lambda policy for ${var.site}"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "logs:DescribeLogStreams"
    ],
      "Resource": [
        "arn:aws:logs:*:*:*"
    ]
  },
      {
        "Effect": "Allow",
        "Action": [
            "cloudfront:CreateInvalidation",
            "cloudfront:ListInvalidations"
        ],
        "Resource": [
            "*"
        ]
    }
 ]
}
EOF
}
resource "aws_iam_policy_attachment" "iam_for_lambda_policy_attachment" {
  name = "iam-for-lambda-${var.site-without-dots}-attachment"
  roles = [
    "${aws_iam_role.iam_for_lambda.name}"]
  policy_arn = "${aws_iam_policy.lambdapolicy.arn}"
}
resource "aws_lambda_permission" "allow_bucket" {
  statement_id = "AllowExecutionFromS3Bucket"
  action = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.func.arn}"
  principal = "s3.amazonaws.com"
  source_arn = "${aws_s3_bucket.b.arn}"
}
