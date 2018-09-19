resource "aws_route53_record" "mail_mx" {
  zone_id = "${aws_route53_zone.dnszone.zone_id}"
  name = ""
  type = "MX"

  records = [
    "15 yduf47ta74y3tslubgaslnijjaqojk6xombzudvjhyjcgkp2jmfq.mx-verification.google.com.",
    "1 ASPMX.L.GOOGLE.COM",
    "5 ALT1.ASPMX.L.GOOGLE.COM",
    "5 ALT2.ASPMX.L.GOOGLE.COM",
    "10 ALT3.ASPMX.L.GOOGLE.COM",
    "10 ALT4.ASPMX.L.GOOGLE.COM",
  ]

  ttl = "3600"
}
