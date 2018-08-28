resource "aws_cloudwatch_event_rule" "every_minute" {
  name = "every-minute"
  description = "Fires every minute"
  schedule_expression = "rate(1 minute)"
}
resource "aws_cloudwatch_event_target" "every_minute_event_target" {
  rule = "${aws_cloudwatch_event_rule.every_minute.name}"
  target_id = "${aws_lambda_function.invalidate.id}"
  arn = "${aws_lambda_function.invalidate.arn}"
  input = "\"automated\""
}
resource "aws_lambda_permission" "allow_cloudwatch_to_call_check_foo" {
  statement_id = "AllowExecutionFromCloudWatch"
  action = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.invalidate.function_name}"
  principal = "events.amazonaws.com"
  source_arn = "${aws_cloudwatch_event_rule.every_minute.arn}"
}