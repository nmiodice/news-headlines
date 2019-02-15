package com.iodice.news.entity

data class Article(
    val text: String,
    val images: Collection<String>,
    val host: String,
    val url: String
)