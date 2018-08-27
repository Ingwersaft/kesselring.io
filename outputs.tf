output "dns-servers" {
  value = "${aws_route53_zone.dnszone.name_servers}"
}
output "cf-distribution" {
  value = "${aws_cloudfront_distribution.cloudfront.domain_name}"
}