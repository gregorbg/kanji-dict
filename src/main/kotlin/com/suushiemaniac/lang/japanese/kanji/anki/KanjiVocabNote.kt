package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.anki.AnkiExporter.JSON
import com.suushiemaniac.lang.japanese.kanji.anki.model.KanjiVocabPhrase
import com.suushiemaniac.lang.japanese.kanji.anki.model.KanjiVocabPhraseToken
import com.suushiemaniac.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.MorphologyToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.SymbolToken
import com.suushiemaniac.lang.japanese.kanji.model.vocabulary.Translation
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString

data class KanjiVocabNote(
    val vocabItem: SymbolToken,
    val mainTranslation: String,
    val additionalTranslations: List<String>,
    val ankiPhrases: List<KanjiVocabPhrase>,
    val originalKanji: Char
) : AnkiDeckNote {
    override fun getCSVFacts(): List<String> {
        return listOf(
            vocabItem.surfaceForm,
            vocabItem.asFurigana(RubyFuriganaFormatter),
            mainTranslation,
            JSON.encodeToString(ListSerializer(String.serializer()), additionalTranslations),
            JSON.encodeToString(
                ListSerializer(ListSerializer(String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getLiterals)
            ),
            JSON.encodeToString(
                ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getReadings)
            ),
            JSON.encodeToString(
                ListSerializer(
                    MapSerializer(
                        String.serializer(),
                        MapSerializer(String.serializer(), String.serializer())
                    )
                ),
                ankiPhrases.map(KanjiVocabPhrase::getTokenData)
            ),
            JSON.encodeToString(
                ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getAnnotations)
            )
        )
    }

    override fun getTags() =
        listOf(originalKanji.toString())

    companion object {
        const val SKIP_TOKEN_LITERAL = "_"
        val SKIP_TOKEN = MorphologyToken(SKIP_TOKEN_LITERAL, SKIP_TOKEN_LITERAL)

        fun from(
            item: VocabularyItem,
            translation: Translation,
            samplePhrases: List<SampleSentence>,
            originalKanji: Char,
            translationSource: TranslationSource
        ): KanjiVocabNote {
            val ankiPhrases = samplePhrases
                .map { it.toVocabTokens(translationSource) }
                .map { it.maskOriginalWordToken(item) }
                .map { KanjiVocabPhrase(it) }

            return KanjiVocabNote(
                item,
                translation.mainTranslation,
                translation.otherTranslations,
                ankiPhrases,
                originalKanji
            )
        }

        private fun SampleSentence.toVocabTokens(translationSource: TranslationSource): List<KanjiVocabPhraseToken> =
            this.tokens.map { KanjiVocabPhraseToken.from(it, translationSource) }

        private fun List<KanjiVocabPhraseToken>.maskOriginalWordToken(item: VocabularyItem): List<KanjiVocabPhraseToken> {
            return map {
                it.takeUnless { pt -> pt.surfaceForm == item.surfaceForm }
                    ?: KanjiVocabPhraseToken(SKIP_TOKEN)
            }
        }
    }
}