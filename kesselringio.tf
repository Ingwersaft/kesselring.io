provider "aws" {
  region = var.region
  profile = "kesselring"
}
provider "aws" {
  alias = "useast"
  region = "us-east-1"
  profile = "kesselring"
}
terraform {
  backend "s3" {
    bucket = "terraform-state-kesselring"
    key = "kesselring.io"
    region = "eu-central-1"
    profile = "kesselring"
  }
}
resource "aws_s3_bucket" "b" {
  bucket = var.site
  acl = "private"
  policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": [
        "s3:GetObject"
      ],
      "Resource": [
        "arn:aws:s3:::${var.site}/*"
      ]
    }
  ]
}
    POLICY
  website {
    index_document = "index.html"
    error_document = "error.html"
  }

  force_destroy = true
  tags = {
    type = var.tag
  }
}

resource "aws_route53_zone" "dnszone" {
  name = var.site
  tags = {
    type = var.tag
  }
}
resource "aws_acm_certificate" "cert" {
  provider = aws.useast
  domain_name = var.site
  validation_method = "DNS"
  subject_alternative_names = [
    "www.${var.site}"]
  tags = {
    type = var.tag
  }
}

resource "aws_route53_record" "cert_validation" {
  name = aws_acm_certificate.cert.domain_validation_options[0].resource_record_name
  type = aws_acm_certificate.cert.domain_validation_options[0].resource_record_type
  zone_id = aws_route53_zone.dnszone.id
  records = [
    aws_acm_certificate.cert.domain_validation_options[0].resource_record_value]
  ttl = 60
}
resource "aws_route53_record" "wwwcert_validation" {
  name = aws_acm_certificate.cert.domain_validation_options[1].resource_record_name
  type = aws_acm_certificate.cert.domain_validation_options[1].resource_record_type
  zone_id = aws_route53_zone.dnszone.id
  records = [
    aws_acm_certificate.cert.domain_validation_options[1].resource_record_value]
  ttl = 60
}
resource "aws_acm_certificate_validation" "cert" {
  provider = aws.useast
  certificate_arn = aws_acm_certificate.cert.arn
  validation_record_fqdns = [
    aws_route53_record.cert_validation.fqdn,
    aws_route53_record.wwwcert_validation.fqdn]
}
resource "aws_cloudfront_distribution" "cloudfront" {
  origin {
    origin_id = var.site
    domain_name = aws_s3_bucket.b.bucket_domain_name
  }

  # If using route53 aliases for DNS we need to declare it here too, otherwise we'll get 403s.
  aliases = [
    var.site,
    "www.${var.site}"]

  enabled = true
  default_root_object = "index.html"
  is_ipv6_enabled = true

  default_cache_behavior {
    allowed_methods = [
      "GET",
      "HEAD",
      "OPTIONS"]
    cached_methods = [
      "GET",
      "HEAD"]
    target_origin_id = var.site

    forwarded_values {
      query_string = true
      cookies {
        forward = "none"
      }
    }

    viewer_protocol_policy = "redirect-to-https"
    min_ttl = 0
    default_ttl = 3600
    max_ttl = 86400
  }

  price_class = "PriceClass_100"

  restrictions {
    geo_restriction {
      restriction_type = "none"
      locations = []
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
    acm_certificate_arn = aws_acm_certificate_validation.cert.certificate_arn
    ssl_support_method = "sni-only"
  }
  tags = {
    type = var.tag
  }
}
