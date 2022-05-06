package net.gregorbg.lang.japanese.kanji.source.nhknews

import net.gregorbg.lang.japanese.kanji.model.nhknews.NewsListResponse
import net.gregorbg.lang.japanese.kanji.model.vocabulary.MorphologyText
import net.gregorbg.lang.japanese.kanji.source.ComplexTextSource
import net.gregorbg.lang.japanese.kanji.source.nhknews.ktor.TrimNHKWhitespaceFeature
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement
import kotlinx.coroutines.runBlocking

object NewsWeb : ComplexTextSource<MorphologyText> {
    private val HTTP_CLIENT = HttpClient(Apache) {
        install(TrimNHKWhitespaceFeature)
        install(ContentNegotiation) {
            json()
        }
    }

    const val TOP_LIST_API = "https://www3.nhk.or.jp/news/json16/syuyo.json"

    val TOP_ARTICLES by lazy { runBlocking { HTTP_CLIENT.get(TOP_LIST_API).body<NewsListResponse>() } }
    val ALL_ARTICLES by lazy { fetchArticlesListRecursive() }

    private fun fetchArticlePage(page: Int): NewsListResponse {
        val urlPageNum = page.toString().padStart(3, '0')
        val apiEndpoint = "https://www3.nhk.or.jp/news/json16/new_$urlPageNum.json"

        return runBlocking { HTTP_CLIENT.get(apiEndpoint).body() }
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

        val fullText = skrape(BrowserFetcher) {
            request {
                url = "https://www3.nhk.or.jp/news/$linkSuffix"
            }

            response {
                htmlDocument {
                    findFirst("p.content--summary").text +
                            findFirst("p.content--summary-more").text +
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