package com.iodice.news.lambda.entity

data class PageCrawlResponse(
    var url: String,
    var remainingDepth: Long,
    var links: Collection<String>
) {
    // used for json deserialization
    constructor() : this("", -1, emptyList())
}
