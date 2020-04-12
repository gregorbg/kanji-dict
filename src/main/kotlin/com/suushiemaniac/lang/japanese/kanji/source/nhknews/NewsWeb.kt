package com.suushiemaniac.lang.japanese.kanji.source.nhknews

import com.suushiemaniac.lang.japanese.kanji.model.nhknews.NewsListResponse
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompositeReadingTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.ReadingToken
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.source.TextSource
import com.suushiemaniac.lang.japanese.kanji.source.nhknews.ktor.TrimNHKWhitespaceFeature
import com.suushiemaniac.lang.japanese.kanji.util.ConvertedReadingTokens
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import it.skrape.core.fetcher.Mode
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.html5.p
import it.skrape.skrape
import kotlinx.coroutines.runBlocking

object NewsWeb : TextSource {
    private val HTTP_CLIENT = HttpClient(Apache) {
        install(TrimNHKWhitespaceFeature)
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    const val TOP_LIST_API = "https://www3.nhk.or.jp/news/json16/syuyo.json"

    val TOP_ARTICLES by lazy { runBlocking { HTTP_CLIENT.get<NewsListResponse>(TOP_LIST_API) } }
    val ALL_ARTICLES by lazy { fetchArticlesListRecursive() }

    private fun fetchArticlePage(page: Int): NewsListResponse {
        val urlPageNum = page.toString().padStart(3, '0')
        val apiEndpoint = "https://www3.nhk.or.jp/news/json16/new_$urlPageNum.json"

        return runBlocking { HTTP_CLIENT.get<NewsListResponse>(apiEndpoint) }
    }

    private tailrec fun fetchArticlesListRecursive(page: Int = 1, accu: NewsListResponse? = null): NewsListResponse {
        val currentPage = fetchArticlePage(page)

        val mergedChannel = currentPage.channel.copy(
            item = accu?.channel?.item.orEmpty() + currentPage.channel.item
        )

        val merged = currentPage.copy(channel = mergedChannel)

        if (!merged.channel.hasNext) {
            return merged
        }

        return fetchArticlesListRecursive(page.inc(), merged)
    }

    override fun getAvailableIDs(): Set<String> {
        return ALL_ARTICLES.channel.item.mapTo(mutableSetOf()) { it.id }
    }

    override fun getText(id: String): ReadingToken {
        val linkSuffix = ALL_ARTICLES.channel.item.find { it.id == id }
            ?.link ?: error("Invalid NHK news ID: $id")

        val fullText = skrape {
            url = "https://www3.nhk.or.jp/news/$linkSuffix"
            mode = Mode.DOM

            extract {
                htmlDocument {
                    p {
                        withClass = "content--summary"

                        findFirst { text }
                    } + p {
                        withClass = "content--summary-more"

                        findFirst { text }
                    }
                }
            }
        }

        val parsed = SampleSentence.parse(fullText)
        val readingTokens = parsed.tokens.flatMap { it.toReadings().tokens }

        return ConvertedReadingTokens(readingTokens)
    }
}