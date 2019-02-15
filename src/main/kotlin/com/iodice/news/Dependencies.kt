package com.iodice.news

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.iodice.news.client.HttpFacade
import com.iodice.news.service.ArticleRetrieveService

class Dependencies : AbstractModule() {

    override fun configure() {
        bind(HttpFacade::class.java) to ArticleRetrieveService::class.java
    }

    @Provides
    fun sns() = AmazonSNSClientBuilder.defaultClient()

    @Provides
    fun ddb() = AmazonDynamoDBClientBuilder.defaultClient()
}