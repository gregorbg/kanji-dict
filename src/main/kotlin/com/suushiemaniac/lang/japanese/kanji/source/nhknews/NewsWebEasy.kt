package com.suushiemaniac.lang.japanese.kanji.source.nhknews

import com.suushiemaniac.lang.japanese.kanji.model.nhknews.easy.EasyNewsListItem
import com.suushiemaniac.lang.japanese.kanji.model.nhknews.easy.TopNewsListItem
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.source.TextSource
import com.suushiemaniac.lang.japanese.kanji.source.nhknews.ktor.TrimNHKWhitespaceFeature
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking

object NewsWebEasy : TextSource {
    private val HTTP_CLIENT = HttpClient(Apache) {
        install(TrimNHKWhitespaceFeature)
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    const val TOP_LIST_API = "https://www3.nhk.or.jp/news/easy/top-list.json"
    const val ARTICLES_BY_DATE_API = "https://www3.nhk.or.jp/news/easy/news-list.json"

    val TOP_ARTICLES by lazy { runBlocking { HTTP_CLIENT.get<List<TopNewsListItem>>(TOP_LIST_API) } }

    val ALL_ARTICLES_BY_DATE by lazy {
        runBlocking {
            HTTP_CLIENT.get<List<Map<String, List<EasyNewsListItem>>>>(ARTICLES_BY_DATE_API)
        }.singleOrNull().orEmpty()
    }

    override fun getAvailableIDs(): Set<String> {
        val flatArticles = ALL_ARTICLES_BY_DATE.values.flatten()
        return flatArticles.mapTo(mutableSetOf()) { it.newsId }
    }

    override fun getText(id: String): List<SampleSentence> {
        TODO("Not yet implemented")
    }
}