package net.gregorbg.lang.japanese.kanji.source.nhknews.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.*
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.*
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer

class TrimNHKWhitespaceFeature internal constructor() {
    class DummyConfig

    companion object : HttpClientPlugin<DummyConfig, TrimNHKWhitespaceFeature> {
        const val CHAR_ZERO_LENGTH_NBSP = '﻿'
        val TRIM_CHARS = charArrayOf(CHAR_ZERO_LENGTH_NBSP)

        override val key: AttributeKey<TrimNHKWhitespaceFeature> = AttributeKey("TrimNHKWhitespaceFeature")

        override fun install(plugin: TrimNHKWhitespaceFeature, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) return@intercept

                val dumbString = body.readRemaining().readText()
                val cleanString = dumbString.trim(*TRIM_CHARS)

                val writer = writer { channel.writeFully(cleanString.toByteArray()) }

                val response = HttpResponseContainer(info, writer.channel)
                proceedWith(response)
            }
        }

        override fun prepare(block: DummyConfig.() -> Unit): TrimNHKWhitespaceFeature {
            return TrimNHKWhitespaceFeature()
        }
    }
}