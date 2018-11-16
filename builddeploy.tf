resource "aws_iam_role" "codebuild_role" {
  name = "${var.site-without-dots}-codebuild-role"
  force_detach_policies = true
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "codebuild.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}
// these are the rights available inside the codebuild script, assumed buy the aws_iam_role role for the codebuild resource
resource "aws_iam_role_policy" "codebuild_policy" {
  role = "${aws_iam_role.codebuild_role.name}"

  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Resource": [
        "*"
      ],
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ]
    },
    {
      "Effect": "Allow",
      "Action": [
        "s3:*"
      ],
      "Resource": [
        "${aws_s3_bucket.b.arn}",
        "${aws_s3_bucket.b.arn}/*"
      ]
    },
    {
      "Effect": "Allow",
      "Action": "cloudfront:CreateInvalidation",
      "Resource": "*"
    }
  ]
}
POLICY
}
resource "aws_codebuild_project" "build" {
  name = "${var.site-without-dots}-codebuild-project"
  description = "automatic codebuild project for ${var.site} managed by terraform"
  build_timeout = "10"
  service_role = "${aws_iam_role.codebuild_role.arn}"

  artifacts {
    type = "NO_ARTIFACTS"
  }

  cache {
    type = "NO_CACHE"
  }

  environment {
    compute_type = "BUILD_GENERAL1_SMALL"
    image = "aws/codebuild/java:openjdk-8"
    type = "LINUX_CONTAINER"

    environment_variable {
      "name" = "S3_BUCKET"
      "value" = "s3://${aws_s3_bucket.b.bucket}"
    }
    environment_variable {
      "name" = "CLOUDFRONT_DISTRIBUTION_ID"
      "value" = "${aws_cloudfront_distribution.cloudfront.id}"
    }
    environment_variable {
      "name" = "SLACK_WEBHOOK"
      "value" = "${var.slack-webhook}"
    }
    environment_variable {
      "name" = "GITHUB_REPO_NAME"
      "value" = "${var.github-repo}"
    }
  }

  source {
    type = "GITHUB"
    location = "https://github.com/${var.github-user}/${var.github-repo}"
    git_clone_depth = 1
    auth {
      type = "OAUTH"
    }
  }
  tags {
    "type" = "${var.tag}"
  }
}
resource "aws_codebuild_webhook" "github-hook" {
  project_name = "${aws_codebuild_project.build.name}"
}
