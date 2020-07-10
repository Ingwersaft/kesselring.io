resource "aws_route53_record" "root_domain" {
  zone_id = aws_route53_zone.dnszone.zone_id
  name = var.site
  type = "A"

  alias {
    name = aws_cloudfront_distribution.cloudfront.domain_name
    zone_id = aws_cloudfront_distribution.cloudfront.hosted_zone_id
    evaluate_target_health = false
  }
}
resource "aws_route53_record" "root_domain_v6" {
  zone_id = aws_route53_zone.dnszone.zone_id
  name = var.site
  type = "AAAA"

  alias {
    name = aws_cloudfront_distribution.cloudfront.domain_name
    zone_id = aws_cloudfront_distribution.cloudfront.hosted_zone_id
    evaluate_target_health = false
  }
}
resource "aws_route53_record" "www_domain" {
  zone_id = aws_route53_zone.dnszone.zone_id
  name = "www.${var.site}"
  type = "A"

  alias {
    name = aws_cloudfront_distribution.cloudfront.domain_name
    zone_id = aws_cloudfront_distribution.cloudfront.hosted_zone_id
    evaluate_target_health = false
  }
}
resource "aws_route53_record" "www_domain_v6" {
  zone_id = aws_route53_zone.dnszone.zone_id
  name = "www.${var.site}"
  type = "AAAA"

  alias {
    name = aws_cloudfront_distribution.cloudfront.domain_name
    zone_id = aws_cloudfront_distribution.cloudfront.hosted_zone_id
    evaluate_target_health = false
  }
}
