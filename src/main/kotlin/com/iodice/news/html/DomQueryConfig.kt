package com.iodice.news.html

import org.jsoup.select.Elements
import java.net.URL

data class DomQueryConfig(
    val host: String,
    val articleSelectors: List<String>,
    val imageSelectors: List<String>,
    val articleTransformer: (Elements) -> String,
    val imageTransformer: (Elements) -> Collection<String>
) {
    companion object {

        // transforms an article into a block of text by stripping out
        // common elements from web articles
        private val SHARED_ARTICLE_TRANSFORMER = fun(elements: Elements): String =
            elements.map { it.text() }
                .map { it.replace(Regex("\\s\\s"), " ") }
                .map { it.replace(Regex("[\\r\\n]+"), " ") }
                .filter { it.isNotEmpty() }
                .joinToString(" ")

        // configuration for bbc.com and bbc.co.uk
        private val BBC_DOT_COM = DomQueryConfig(
            host = "www.bbc.com",
            articleSelectors = listOf(
                "*[property=articleBody] p:not(.off-screen)"
            ),
            imageSelectors = listOf(
                "*[property=articleBody] div[data-src]:not([data-height=\"1\"])"
            ),
            imageTransformer = fun(e: Elements): Collection<String> =
                e.map { it.attr("data-src") },
            articleTransformer = SHARED_ARTICLE_TRANSFORMER
        )

        /**
         * @return a [DomQueryConfig] specific to the url
         * @throws [IllegalStateException] if no config can be found for the url
         */
        @JvmStatic
        fun getConfig(url: String): DomQueryConfig {
            return when (URL(url).host) {
                BBC_DOT_COM.host -> BBC_DOT_COM
                else -> throw IllegalStateException("No configuration for ${URL(url).host}")
            }
        }
    }
}
