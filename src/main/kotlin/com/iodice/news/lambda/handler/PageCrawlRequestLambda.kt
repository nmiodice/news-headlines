package com.iodice.news.lambda.handler

import com.iodice.news.lambda.LambdaConfig
import com.iodice.news.lambda.entity.PageCrawlRequest
import com.iodice.news.lambda.entity.PageCrawlResponse
import com.iodice.news.service.PageCrawlService

class PageCrawlRequestLambda : SnsHandler<PageCrawlRequest, PageCrawlResponse>(
    PageCrawlRequest::class.java
) {
    private val crawlService = LambdaConfig.INJECTOR.getInstance(PageCrawlService::class.java)

    override fun compute(input: PageCrawlRequest) = listOf(
        PageCrawlResponse(
            url = input.url,
            remainingDepth = input.remainingDepth,
            links = crawlService.crawl(input.url)
        )
    )
}