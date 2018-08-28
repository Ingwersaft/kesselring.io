resource "aws_s3_bucket_object" "web" {
  bucket = "${aws_s3_bucket.b.bucket}"
  key = "index.html"
  source = "site/src/main/web/index.html"
  content_type = "text/html"
  etag = "${md5(file("site/src/main/web/index.html"))}"
}
resource "aws_s3_bucket_object" "error" {
  bucket = "${aws_s3_bucket.b.bucket}"
  key = "error.html"
  source = "site/src/main/web/error.html"
  content_type = "text/html"
  etag = "${md5(file("site/src/main/web/error.html"))}"
}
