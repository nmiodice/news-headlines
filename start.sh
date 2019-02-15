#!/bin/bash

source commons.sh

FUNCTION_NAME=$(aws lambda list-functions --no-paginate  \
    | grep 'PageCrawlRequest'                            \
    | grep 'FunctionName'                                \
    | awk  '{print $2}'                                  \
    | sed -e 's/^,//' -e 's/,$//'                        \
    | sed -e 's/^"//' -e 's/"$//'                        \
)

OUTPUT_FILE=".out.txt"

log "found function: $FUNCTION_NAME"


SOURCES=('http://www.bbc.com/' 'http://abcnews.go.com/')

for SOURCE in "${SOURCES[@]}"
do
    PRE='{"Records": [{"Sns": {"Message": "{\"url\" : \"'
    POST='\", \"remainingDepth\" : 1}"}}]}'
    PAYLOAD="$PRE$SOURCE$POST"

    log "using payload: $PAYLOAD"
    aws lambda invoke --function-name $FUNCTION_NAME --payload "$PAYLOAD" $OUTPUT_FILE
done

rm -rf $OUTPUT_FILE
log "done"
