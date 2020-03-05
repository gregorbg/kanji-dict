package com.suushiemaniac.lang.japanese.kanji.source.jisho

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.util.alignReadingsWith
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking

class JishoPublicAPI(val kanjiSource: KanjiSource) : VocabularySource {
    private val HTTP_CLIENT = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    private fun searchURL(keyword: String) =
        "$API_URL?keyword=$keyword"

    private fun executeSearch(keyword: String) =
        runBlocking { HTTP_CLIENT.get<JishoAPIResponse>(searchURL(keyword)) }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        val searchKanji = kanji.kanji.toString()
        val result = executeSearch(searchKanji)

        return result.data.map { it.toVocabularyItem(searchKanji) }
    }

    override fun lookupWord(raw: String): VocabularyItem? {
        val result = executeSearch(raw)

        val suitableItem = result.data.firstOrNull {
            it.japanese.any { j -> j.word == raw || j.reading == raw }
        }

        return suitableItem?.toVocabularyItem(raw)
    }

    private fun JishoAPIData.toVocabularyItem(origQuery: String): VocabularyItem {
        val suitableReading = this.japanese.first { origQuery in it.word }
        val mappedReading = suitableReading.word.alignReadingsWith(suitableReading.reading, kanjiSource)

        val translations = this.senses.flatMap { it.englishDefinitions }.distinct()

        return VocabularyItem(mappedReading, translations)
    }

    companion object {
        const val API_URL = "https://jisho.org/api/v1/search/words"
    }
}