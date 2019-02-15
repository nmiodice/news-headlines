package com.iodice.news.client

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager
import org.apache.commons.httpclient.methods.GetMethod
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.nio.charset.Charset
import javax.inject.Singleton

data class HttpRequestParams(val url: String)

class HttpException(val status: Int, val reason: String) : Exception()

@Singleton
class HttpFacade {

    private val httpClient = HttpClient(MultiThreadedHttpConnectionManager())

    fun get(params: HttpRequestParams): Document {
        val request = toHttpGet(params)
        var response = -1

        try {
            response = httpClient.executeMethod(request)
            val stream = request.responseBodyAsStream
            return stream.readBytes().toString(Charset.defaultCharset()).let {
                stream.close()
                Jsoup.parse(it, params.url)
            }
        } catch (e: Exception) {
            println("$response, ${e.localizedMessage}")
            throw HttpException(status = response, reason = e.localizedMessage)
        } finally {
            request.releaseConnection()
        }
    }

    private fun toHttpGet(params: HttpRequestParams): GetMethod {
        return GetMethod(params.url).let { method ->
            method.followRedirects = true
            method
        }
    }
}
