package com.suushiemaniac.lang.japanese.kanji.source.jisho

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.jisho.JishoAPIData
import com.suushiemaniac.lang.japanese.kanji.model.jisho.JishoAPIResponse
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.util.alignReadingsWith
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking

class JishoPublicAPI(val kanjiSource: KanjiSource) : VocabularySource, TranslationSource {
    private fun searchURL(keyword: String, page: Int = 1) =
        "$API_URL?keyword=$keyword&page=$page"

    private fun executeSearch(keyword: String, page: Int = 1) =
        runBlocking { HTTP_CLIENT.get<JishoAPIResponse>(searchURL(keyword, page)) }

    private fun executePaginatedSearch(keyword: String): JishoAPIResponse {
        val rawResponse = accumulateSearch(keyword, 1)

        val cleanData = rawResponse.data.map { it.cleanup() }
        return rawResponse.copy(data = cleanData)
    }

    private fun accumulateSearch(keyword: String, page: Int): JishoAPIResponse {
        val current = executeSearch(keyword, page)

        if (current.data.size == JISHO_PAGE_MAX) {
            val nextPage = accumulateSearch(keyword, page + 1)

            val mergedData = current.data + nextPage.data
            return nextPage.copy(data = mergedData)
        }

        return current
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        val kanjiLiteral = kanji.kanji.toString()

        val searchQuery = "*$kanjiLiteral*"
        val result = executePaginatedSearch(searchQuery)

        return result.data.mapNotNull { it.toVocabularyItem(kanjiLiteral) }
    }

    override fun lookupWord(raw: String): VocabularyItem? {
        val result = executePaginatedSearch(raw)

        val suitableItem = result.data.firstOrNull {
            it.japanese.any { j -> j.word == raw || j.reading == raw }
        }

        return suitableItem?.toVocabularyItem(raw)
    }

    private fun JishoAPIData.toVocabularyItem(origQuery: String): VocabularyItem? {
        val suitableReading = this.japanese.firstOrNull { origQuery in it.word.orEmpty() }
            ?: return null

        val mappedReading = suitableReading.word.orEmpty()
            .alignReadingsWith(suitableReading.reading, kanjiSource)

        val translations = this.senses.flatMap { it.englishDefinitions }.distinct()

        return VocabularyItem(mappedReading, translations)
    }

    companion object {
        const val API_URL = "https://jisho.org/api/v1/search/words"
        const val JISHO_PAGE_MAX = 20

        private val HTTP_CLIENT = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        private fun JishoAPIData.cleanup(): JishoAPIData {
            val rawReadings = this.japanese
            val cleanReadings = rawReadings.mapIndexed { i, rd ->
                if (rd.word == null) {
                    val lastWord = rawReadings.take(i).findLast { it.word != null }

                    rd.copy(word = lastWord?.word)
                } else
                    rd
            }

            return this.copy(japanese = cleanReadings)
        }
    }
}