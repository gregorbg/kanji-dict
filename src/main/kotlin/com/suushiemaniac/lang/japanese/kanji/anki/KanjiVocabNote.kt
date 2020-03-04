package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.anki.AnkiExporter.JSON
import com.suushiemaniac.lang.japanese.kanji.anki.model.KanjiVocabPhrase
import com.suushiemaniac.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

data class KanjiVocabNote(
    val vocabItem: ReadingWithSurfaceForm,
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
            JSON.stringify(ListSerializer(String.serializer()), additionalTranslations),
            JSON.stringify(ListSerializer(ListSerializer(String.serializer())), ankiPhrases.map(KanjiVocabPhrase::getLiterals)),
            JSON.stringify(
                ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getReadings)
            ),
            JSON.stringify(
                ListSerializer(MapSerializer(String.serializer(), MapSerializer(String.serializer(), String.serializer()))),
                ankiPhrases.map(KanjiVocabPhrase::getTokenData)
            ),
            JSON.stringify(
                ListSerializer(MapSerializer(String.serializer(), String.serializer())),
                ankiPhrases.map(KanjiVocabPhrase::getAnnotations)
            )
        )
    }

    override fun getTags() =
        listOf(originalKanji.toString())

    companion object {
        fun from(item: VocabularyItem, originalKanji: Char): KanjiVocabNote {
            return KanjiVocabNote(
                item,
                item.translations.first(),
                item.translations.drop(1),
                listOf(),
                originalKanji
            )
        }
    }
}