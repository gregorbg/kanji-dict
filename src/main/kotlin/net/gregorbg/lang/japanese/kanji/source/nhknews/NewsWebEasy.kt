package net.gregorbg.lang.japanese.kanji.source.nhknews

import net.gregorbg.lang.japanese.kanji.model.nhknews.easy.EasyNewsListItem
import net.gregorbg.lang.japanese.kanji.model.nhknews.easy.TopNewsListItem
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.ReadingText
import net.gregorbg.lang.japanese.kanji.source.ComplexTextSource
import net.gregorbg.lang.japanese.kanji.source.nhknews.ktor.TrimNHKWhitespaceFeature
import net.gregorbg.lang.japanese.kanji.util.parseRuby
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.div
import kotlinx.coroutines.runBlocking

object NewsWebEasy : ComplexTextSource<WordLevelToken> {
    private val HTTP_CLIENT get() = HttpClient(Java) {
        install(TrimNHKWhitespaceFeature)
        install(ContentNegotiation) {
            json()
        }
    }

    const val TOP_LIST_API = "https://www3.nhk.or.jp/news/easy/top-list.json"
    const val ARTICLES_BY_DATE_API = "https://www3.nhk.or.jp/news/easy/news-list.json"

    val TOP_ARTICLES: List<TopNewsListItem> by lazy { runBlocking { HTTP_CLIENT.get(TOP_LIST_API).body() } }

    val ALL_ARTICLES_BY_DATE: Map<String, List<EasyNewsListItem>> by lazy {
        runBlocking {
            HTTP_CLIENT.get(ARTICLES_BY_DATE_API).body<List<Map<String, List<EasyNewsListItem>>>>()
        }.singleOrNull().orEmpty()
    }

    override fun getAvailableIDs(): Set<String> {
        val flatArticles = ALL_ARTICLES_BY_DATE.values.flatten()
        return flatArticles.map { it.newsId }.toSet()
    }

    override fun getText(id: String): ReadingText {
        val readingTokens = skrape(HttpFetcher) {
            request {
                url = "https://www3.nhk.or.jp/news/easy/$id/$id.html"
            }

            response {
                htmlDocument {
                    div {
                        withClass = "article-main__body"

                        findFirst { parseRuby() }
                    }
                }
            }
        }

        return ReadingText.fromTokens(readingTokens)
    }
}