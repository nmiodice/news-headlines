package com.iodice.news.lambda.handler

import com.iodice.news.lambda.LambdaConfig
import com.iodice.news.lambda.entity.PageCrawlRequest
import com.iodice.news.lambda.entity.PageCrawlResponse
import com.iodice.news.service.ArticlePersistService
import com.iodice.news.service.ArticleRetrieveService

class PageCrawlResponseLambda : SnsHandler<PageCrawlResponse, PageCrawlRequest>(
    PageCrawlResponse::class.java
) {

    private val articleRetrieveService = LambdaConfig.INJECTOR
        .getInstance(ArticleRetrieveService::class.java)

    private val articlePersistService = LambdaConfig.INJECTOR
        .getInstance(ArticlePersistService::class.java)

    override fun compute(input: PageCrawlResponse): Collection<PageCrawlRequest> {
        persistArticles(input.links)
        return next(input)
    }

    private fun persistArticles(urls: Collection<String>) = urls
        .map { articleRetrieveService.retrieve(it) }
        .filter { it.text.isNotEmpty() }
        .forEach { articlePersistService.persist(it) }

    fun next(response: PageCrawlResponse) =
        if (response.remainingDepth > 0) response.links.map {
            PageCrawlRequest(it, response.remainingDepth - 1)
        } else {
            emptyList()
        }
}