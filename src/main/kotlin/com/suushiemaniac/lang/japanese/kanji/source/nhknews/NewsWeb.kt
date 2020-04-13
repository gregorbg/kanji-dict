package com.suushiemaniac.lang.japanese.kanji.source.nhknews

import com.suushiemaniac.lang.japanese.kanji.model.nhknews.NewsListResponse
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.MorphologyText
import com.suushiemaniac.lang.japanese.kanji.source.ComplexTextSource
import com.suushiemaniac.lang.japanese.kanji.source.nhknews.ktor.TrimNHKWhitespaceFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import it.skrape.core.fetcher.Mode
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.DocElement
import it.skrape.selects.html5.p
import it.skrape.skrape
import kotlinx.coroutines.runBlocking

object NewsWeb : ComplexTextSource<MorphologyText> {
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

    override fun getText(id: String): MorphologyText {
        val linkSuffix = ALL_ARTICLES.channel.item.find { it.id == id }
            ?.link ?: error("Invalid NHK news ID: $id")

        val fullText = skrape {
            url = "https://www3.nhk.or.jp/news/$linkSuffix"
            mode = Mode.DOM

            extract {
                htmlDocument {
                    findFirst("p.content--summary").text +
                            findFirstOrNull("p.content--summary-more")?.text.orEmpty() +
                            document.select("content--detail-more div.body-text")
                                .map { DocElement(it) }
                                .joinToString("") { text }
                }
            }
        }

        val fullTextNonBlank = fullText.filterNot { it.isWhitespace() }
        return MorphologyText.parse(fullTextNonBlank)
    }
}