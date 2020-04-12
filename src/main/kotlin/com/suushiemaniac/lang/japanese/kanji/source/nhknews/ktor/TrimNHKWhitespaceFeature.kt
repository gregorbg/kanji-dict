package com.suushiemaniac.lang.japanese.kanji.source.nhknews.ktor

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer

internal object TrimNHKWhitespaceFeature : HttpClientFeature<TrimNHKWhitespaceFeature.DummyConfig, TrimNHKWhitespaceFeature> {
    class DummyConfig

    const val CHAR_ZERO_LENGTH_NBSP = '﻿'
    val TRIM_CHARS = charArrayOf(CHAR_ZERO_LENGTH_NBSP)

    override val key: AttributeKey<TrimNHKWhitespaceFeature> = AttributeKey("TrimNHKWhitespaceFeature")

    override fun install(feature: TrimNHKWhitespaceFeature, scope: HttpClient) {
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
        return this
    }
}