package net.gregorbg.lang.japanese.kanji.source.jisho.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.coroutines.delay
import kotlin.math.abs

class JishoRateLimitingFeature internal constructor() {
    companion object : HttpClientPlugin<Unit, JishoRateLimitingFeature> {
        const val JISHO_RATE_LIMIT_MS = 1000
        const val JISHO_RATE_LIMIT_COUNT = 10

        val CURRENT_TIME_MS get() = System.currentTimeMillis()

        private var lastRequest: Long = 0
        private var requestCount: Int = 0

        override val key: AttributeKey<JishoRateLimitingFeature> = AttributeKey("JishoRateLimitingFeature")

        override fun install(plugin: JishoRateLimitingFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {

                if (requestCount > JISHO_RATE_LIMIT_COUNT) {
                    val timeDifference = abs(lastRequest - CURRENT_TIME_MS)

                    if (timeDifference < JISHO_RATE_LIMIT_MS) {
                        val waitTime = JISHO_RATE_LIMIT_MS - timeDifference

                        delay(waitTime)
                    }

                    requestCount = 0
                }

                lastRequest = CURRENT_TIME_MS
                requestCount++

                proceedWith(subject)
            }
        }

        override fun prepare(block: Unit.() -> Unit): JishoRateLimitingFeature {
            return JishoRateLimitingFeature()
        }
    }
}