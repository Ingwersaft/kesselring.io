from __future__ import print_function

import boto3
import time
import os


def handler(event, context):
    path = ['/*']
    print(path)
    client = boto3.client('cloudfront')
    response = client.list_invalidations(
        DistributionId=os.environ['distribution'],
        MaxItems='50'
    )
    items = response['InvalidationList']['Items']
    alreadyInProgress = any(item['Status'] == 'InProgress' for item in items)
    print(alreadyInProgress)
    if alreadyInProgress:
        print('skipping invalidation')
        return
    invalidation = client.create_invalidation(DistributionId=os.environ['distribution'],
                                              InvalidationBatch={
                                                  'Paths': {
                                                      'Quantity': 1,
                                                      'Items': path
                                                  },
                                                  'CallerReference': str(time.time())
                                              })
