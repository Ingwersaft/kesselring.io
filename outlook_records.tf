resource "aws_route53_record" "mail_mx" {
  zone_id = "${aws_route53_zone.dnszone.zone_id}"
  name = ""
  type = "MX"

  records = [
    "0 kesselring-io.mail.protection.outlook.com."
  ]

  ttl = "3600"
}
resource "aws_route53_record" "mail_cname" {
  zone_id = "${aws_route53_zone.dnszone.zone_id}"
  name = "autodiscover.${var.site}"
  type = "CNAME"

  records = [
    "autodiscover.outlook.com."
  ]

  ttl = "3600"
}
resource "aws_route53_record" "mail_txt" {
  zone_id = "${aws_route53_zone.dnszone.zone_id}"
  name = ""
  type = "TXT"

  records = [
    "v=spf1 include:spf.protection.outlook.com -all"
  ]

  ttl = "3600"
}