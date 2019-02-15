package com.iodice.news

import com.google.inject.Guice
import com.iodice.news.service.ArticlePersistService
import com.iodice.news.service.ArticleRetrieveService
import com.iodice.news.service.PageCrawlService

fun main(args: Array<String>) {
    val injector = Guice.createInjector(Dependencies())
    val articleRetrieveService = injector.getInstance(ArticleRetrieveService::class.java)
    val pageCrawlService = injector.getInstance(PageCrawlService::class.java)
    val articlePersistService = injector.getInstance(ArticlePersistService::class.java)

    pageCrawlService.crawl("http://www.bbc.com/")
        .toList()
        .subList(20, 40)
        .map { articleRetrieveService.retrieve(it) }
        .filter { it.text.isNotEmpty() }
        .forEach { articlePersistService.persist(it) }
//        .forEach { println(it) }
}
