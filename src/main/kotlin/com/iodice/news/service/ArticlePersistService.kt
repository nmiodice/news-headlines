package com.iodice.news.service

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.iodice.news.entity.Article
import javax.inject.Inject

@DynamoDBTable(tableName = "Articles")
open class ArticleEntity(
    @DynamoDBHashKey(attributeName = "PK")
    var host: String = "",

    @DynamoDBRangeKey(attributeName = "RK")
    var url: String = "",

    @DynamoDBAttribute
    var text: String = "",

    @DynamoDBAttribute
    var images: List<String> = emptyList()
){
    constructor(article: Article) : this(
        url = article.url,
        text = article.text,
        images = article.images.toList(),
        host = article.host
    )
}

class ArticlePersistService @Inject constructor(
    ddb: AmazonDynamoDB
) {
    private val mapper: DynamoDBMapper = DynamoDBMapper(ddb)

    fun persist(article: Article) {
        println("saving article with url: ${article.url}")
        mapper.save(ArticleEntity(article))
    }
}