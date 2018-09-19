output "dns-servers" {
  value = "${aws_route53_zone.dnszone.name_servers}"
}
output "cf-distribution" {
  value = "${aws_cloudfront_distribution.cloudfront.domain_name}"
}
output "sqs-arn" {
  value = "${aws_sqs_queue.s3_changed_queue.arn}"
}
output "cloudfront-status" {
  value = "${aws_cloudfront_distribution.cloudfront.status}"
}
