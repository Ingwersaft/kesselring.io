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
variable "web" {
  description = "all files to be copied"
  type = "list"
  default = [
    "deutschland_heidelberg_header.jpg",
    "error.html",
    "index.html",
    "index2.html",
    "logo_bad.png",
    "logo_bad_inv.png"]
}
//resource "aws_s3_bucket_object" "webcontent" {
//  count = "${length(var.web)}"
//  bucket = "${aws_s3_bucket.b.bucket}"
//  key = "${element(var.web, count.index)}"
//  source = "site/src/main/web/${element(var.web, count.index)}"
//  //content_type = "text/html"
//  etag = "${md5(file("site/src/main/web/error.html"))}"
//}
//// the bundle is missing
output "test-contant" {
  value = "${concat("a","b")}"
}