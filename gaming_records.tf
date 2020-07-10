resource "aws_route53_record" "empy" {
  name = "empy.${var.site}"
  zone_id = aws_route53_zone.dnszone.zone_id
  type = "CNAME"

  records = [
    "51.75.144.98"
  ]

  ttl = "3600"
}
