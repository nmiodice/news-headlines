#!/bin/bash

source commons.sh

log "building..."
rm -rf target/
mvn package

log "built"