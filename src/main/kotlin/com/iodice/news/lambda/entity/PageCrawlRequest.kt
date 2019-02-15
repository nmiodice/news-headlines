package com.iodice.news.lambda.entity

data class PageCrawlRequest(
    var url: String,
    var remainingDepth: Long
) {
    // used for json deserialization
    constructor() : this("", -1)
}