package com.suushiemaniac.lang.japanese.kanji.anki

import com.suushiemaniac.lang.japanese.kanji.anki.AnkiExporter.JSON
import com.suushiemaniac.lang.japanese.kanji.anki.model.KanjiVocabPhrase
import com.suushiemaniac.lang.japanese.kanji.anki.model.RubyFuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm
import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.list
import kotlinx.serialization.map

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
            JSON.stringify(StringSerializer.list, additionalTranslations),
            JSON.stringify(StringSerializer.list.list, ankiPhrases.map(KanjiVocabPhrase::getLiterals)),
            JSON.stringify(
                (StringSerializer to StringSerializer).map.list,
                ankiPhrases.map(KanjiVocabPhrase::getReadings)
            ),
            JSON.stringify(
                (StringSerializer to (StringSerializer to StringSerializer).map).map.list,
                ankiPhrases.map(KanjiVocabPhrase::getTokenData)
            ),
            JSON.stringify(
                (StringSerializer to StringSerializer).map.list,
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