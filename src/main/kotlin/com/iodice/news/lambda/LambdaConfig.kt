package com.iodice.news.lambda

import com.google.inject.Guice
import com.iodice.news.Dependencies

class LambdaConfig {
    companion object {
        val RESULTS_TOPIC: String = System.getenv("ResultsTopic")
        val INJECTOR = Guice.createInjector(Dependencies())!!
    }
}