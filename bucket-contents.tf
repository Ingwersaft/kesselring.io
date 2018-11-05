variable "web" {
  description = "all files to be copied"
  type = "list"
  default = [
    "deutschland_heidelberg_header.jpg#image/jpeg",
    "error.html#text/html",
    "index.html#text/html",
    "index2.html#text/html",
    "impressum.html#text/html",
    "datenschutz.html#text/html",
    "logo_bad.png#image/png",
    "logo_bad_inv.png#image/png",
    "kesselring.css#text/css",
    "pic2.jpg#image/jpeg"]
}
resource "aws_s3_bucket_object" "webcontent" {
  count = "${length(var.web)}"
  bucket = "${aws_s3_bucket.b.bucket}"
  key = "${element(split("#",element(var.web, count.index)),0)}"
  source = "site/src/main/web/${element(split("#",element(var.web, count.index)),0)}"
  content_type = "${element(split("#",element(var.web, count.index)),1)}"
  etag = "${md5(file(format("%s%s","site/src/main/web/",element(split("#",element(var.web, count.index)),0))))}"
}
//// the bundle is missing
resource "aws_s3_bucket_object" "bundle" {
  bucket = "${aws_s3_bucket.b.bucket}"
  key = "kesselringio.bundle.js"
  source = "site/build/bundle/kesselringio.bundle.js"
  content_type = "application/javascript"
  etag = "${md5(file("site/build/bundle/kesselringio.bundle.js"))}"
}