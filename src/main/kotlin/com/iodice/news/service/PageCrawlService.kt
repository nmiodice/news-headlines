package com.iodice.news.service

import com.iodice.news.client.HttpFacade
import com.iodice.news.client.HttpRequestParams
import java.net.URL
import javax.inject.Inject

class PageCrawlService @Inject constructor(
    private val httpFacade: HttpFacade
) {
    fun crawl(url: String): Collection<String> {
        val dom = httpFacade.get(HttpRequestParams(url))

        val sourceHost = URL(url).host
        return dom.select("a")
            .map { anchor -> anchor.attr("abs:href") }
            .filter { link -> link.isNotEmpty() }
            .filter { link -> sourceHost == URL(link).host }
            .filter { link -> url != link }
            .toSet()
    }
}