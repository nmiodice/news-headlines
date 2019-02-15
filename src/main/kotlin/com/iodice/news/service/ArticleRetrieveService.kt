package com.iodice.news.service

import com.iodice.news.client.HttpFacade
import com.iodice.news.client.HttpRequestParams
import com.iodice.news.entity.Article
import com.iodice.news.html.DomQueryConfig
import org.jsoup.nodes.Document
import java.net.URL
import javax.inject.Inject

class ArticleRetrieveService @Inject constructor(
    private val httpFacade: HttpFacade
) {
    fun retrieve(url: String): Article = httpFacade.get(HttpRequestParams(url)).let {
        Article(
            text = getText(it),
            images = getImages(it),
            host = getHost(url),
            url = url
        )
    }

    private fun getText(dom: Document) = with(DomQueryConfig.getConfig(dom.location())) {
        articleSelectors.map { dom.select(it) }
            .joinToString(" ", transform = articleTransformer)
    }

    private fun getImages(dom: Document) = with(DomQueryConfig.getConfig(dom.location())) {
        imageSelectors.map { dom.select(it) }
            .map(imageTransformer)
            .flatten()
            .toSet()
    }

    private fun getHost(url: String) = URL(url).host
}