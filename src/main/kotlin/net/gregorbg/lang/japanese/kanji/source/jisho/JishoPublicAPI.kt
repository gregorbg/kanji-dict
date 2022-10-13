package net.gregorbg.lang.japanese.kanji.source.jisho

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.model.jisho.JishoAPIData
import net.gregorbg.lang.japanese.kanji.model.jisho.JishoAPIResponse
import net.gregorbg.lang.japanese.kanji.model.jisho.JishoReading
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.source.SampleSentenceSource
import net.gregorbg.lang.japanese.kanji.source.TranslationSource
import net.gregorbg.lang.japanese.kanji.source.VocabularySource
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith
import net.gregorbg.lang.japanese.kanji.util.guessVocabModifiers
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologyToken

class JishoPublicAPI(val kanjiSource: KanjiSource) : VocabularySource, TranslationSource,
    SampleSentenceSource<MorphologyToken> {
    private fun searchURL(keyword: String, page: Int = 1) =
        "$API_URL?keyword=$keyword&page=$page"

    private fun executeSearch(keyword: String, page: Int = 1) =
        runBlocking { HTTP_CLIENT.get(searchURL(keyword, page)).body<JishoAPIResponse>() }

    private fun executePaginatedSearch(keyword: String): JishoAPIResponse {
        return REQUEST_CACHE.getOrPut(keyword) {
            val rawResponse = accumulateSearch(keyword, 1)

            val cleanData = rawResponse.data.map { it.cleanup() }
            rawResponse.copy(data = cleanData)
        }
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

    override fun getTranslationFor(token: TokenWithSurfaceForm): Translation? {
        val result = executePaginatedSearch(token.surfaceForm)

        val suitableItem = result.data.firstOrNull {
            it.japanese.any { j -> j.word == token.surfaceForm || j.reading == token.reading }
        }

        return suitableItem?.extractTranslations()
    }

    override fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem> {
        val kanjiLiteral = kanji.kanji.toString()

        val searchQuery = "*$kanjiLiteral*"
        val result = executePaginatedSearch(searchQuery)

        return result.data.mapNotNull { it.toVocabularyItem(kanjiLiteral) }
    }

    override fun getSampleSentencesFor(vocab: VocabularyItem): List<SampleSentence<MorphologyToken>> {
        val result = executePaginatedSearch(vocab.surfaceForm)

        return result.data.filter {
            it.japanese.any { j -> j.word == vocab.surfaceForm || j.reading == vocab.reading }
        }.flatMap { it.extractSampleSentences() }
    }

    private fun JishoAPIData.toVocabularyItem(origQuery: String): VocabularyItem? {
        val suitableReading = this.japanese.firstOrNull { origQuery in it.word.orEmpty() }
            ?: return null

        val mappedReading = suitableReading.word.orEmpty()
            .alignSymbolsWith(suitableReading.reading.orEmpty(), kanjiSource)

        val guessedModifiers = mappedReading.guessVocabModifiers()

        return VocabularyItem(
            mappedReading,
            guessedModifiers
        )
    }

    private fun JishoAPIData.extractTranslations(): Translation {
        val mainTranslationsGuess = this.senses.mapNotNull { it.englishDefinitions.firstOrNull() }.distinct()
        val additionalTranslations = this.senses.flatMap { it.englishDefinitions.drop(1) }.distinct()

        return Translation(
            mainTranslationsGuess.joinToString(),
            additionalTranslations
        )
    }

    private fun JishoAPIData.extractSampleSentences(): List<SampleSentence<MorphologyToken>> {
        return this.senses.flatMap { it.sentences }.map(SampleSentence.Companion::parseWithMorphology)
    }

    companion object {
        const val API_URL = "https://jisho.org/api/v1/search/words"
        const val JISHO_PAGE_MAX = 20

        private val HTTP_CLIENT = HttpClient(Apache) {
            install(ContentNegotiation) {
                json()
            }
        }

        private val REQUEST_CACHE = mutableMapOf<String, JishoAPIResponse>()

        private fun JishoAPIData.cleanup(): JishoAPIData {
            val rawReadings = this.japanese

            val cleanReadings = rawReadings.mapIndexed { i, rd ->
                rd.cleanup(rawReadings.take(i), this.slug)
            }

            return this.copy(japanese = cleanReadings)
        }

        private fun JishoReading.cleanup(history: List<JishoReading>, backupWord: String? = null): JishoReading {
            if (history.isEmpty()) {
                return this
            }

            if (word == null) {
                val lastWord = history.findLast { it.word != null }
                    ?.word ?: backupWord ?: error("Word sense broken")

                return copy(word = lastWord).cleanup(history)
            }

            if (reading == null) {
                val lastReading = history.findLast { it.reading != null }
                    ?.reading ?: error("Reading sense broken")

                return copy(reading = lastReading).cleanup(history)
            }

            return this
        }
    }
}