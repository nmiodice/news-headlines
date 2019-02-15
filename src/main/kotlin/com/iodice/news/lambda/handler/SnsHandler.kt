package com.iodice.news.lambda.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.PublishRequest
import com.google.gson.Gson
import com.iodice.news.lambda.LambdaConfig

abstract class SnsHandler<in INPUT, out OUTPUT>(
    private val recordMessageType: Class<INPUT>
) : RequestHandler<SNSEvent, Unit> {

    private val sns = LambdaConfig.INJECTOR.getInstance(AmazonSNS::class.java)
    private val gson = Gson()

    override fun handleRequest(event: SNSEvent, context: Context?) = event.records
        .map { inflate(it.sns.message) }
        .map { computeAndLog(it) }
        .flatten()
        .forEach {
            sns.publish(
                PublishRequest(
                    LambdaConfig.RESULTS_TOPIC,
                    gson.toJson(it)
                )
            )
        }

    private fun inflate(data: String): INPUT = gson.fromJson(data, recordMessageType)

    private fun computeAndLog(input: INPUT) = compute(input).let {
        println("input: $input")
        println("output: $it")
        it
    }

    abstract fun compute(input: INPUT): Collection<OUTPUT>
}