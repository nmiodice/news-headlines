#!/bin/bash

BUCKET_NAME="news-headlines"
STACK_NAME="news-headlines-stack"
BUILD_DIR="target/"

source commons.sh

sh build.sh

log "bootstrapping S3"
aws s3 mb "s3://$BUCKET_NAME"


log "uploading to S3"
aws cloudformation package                                    \
    --template-file template.yml                              \
    --output-template-file "$BUILD_DIR/serverless-output.yml" \
    --s3-bucket $BUCKET_NAME


log "deleting previous CFN stacks"
aws cloudformation delete-stack --stack-name $STACK_NAME


log "waiting for delete to complete..."
aws cloudformation wait stack-delete-complete --stack-name $STACK_NAME


log "cleaning old log groups..."
LOG_GROUPS=$(aws logs describe-log-groups --no-paginate  \
    | grep $STACK_NAME                                   \
    | grep 'logGroupName'                                \
    | awk  '{print $2}'                                  \
    | sed -e 's/^,//' -e 's/,$//'                        \
    | sed -e 's/^"//' -e 's/"$//'                        \
)
while read -r GROUP; do
    if [ ! -z "$GROUP" ]
    then
        log "deleting log group: $GROUP"
        aws logs delete-log-group --log-group-name $GROUP
    fi
done <<< "$LOG_GROUPS"


log "deploying"
aws cloudformation deploy                               \
    --template-file "$BUILD_DIR/serverless-output.yml"  \
    --stack-name "$STACK_NAME"                          \
    --capabilities CAPABILITY_IAM

log "done"
