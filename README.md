# kesselring.io

source of kesselring.io using:
 * kotlinjs
 * plain html
 * bulma css
 * terraform for IaC and deployment
 
## automatic invalidation
cloudfront invalidation after changes is automated using:
 * sqs queue for change events
 * s3 bucket configured to create these events
 * lambda function consuming those events and triggering 
 cf invalidation if no invalidation is currently running