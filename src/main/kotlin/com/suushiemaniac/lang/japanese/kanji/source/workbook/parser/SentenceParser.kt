package com.suushiemaniac.lang.japanese.kanji.source.workbook.parser

import com.suushiemaniac.lang.japanese.kanji.model.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.OnYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanaReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanjiReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.util.containsOnlyHiragana
import com.suushiemaniac.lang.japanese.kanji.util.containsOnlyKatakana
import com.suushiemaniac.lang.japanese.kanji.util.pluckKanji

abstract class NewlineGroupParser<T>(rawContent: String) : FileParser<T>(rawContent) {
    override fun getAssociations(): Map<String, T> {
        val paragraphs = rawContent.split("\r\n\r\n", "\n\n", "\r\r")

        val symbolGroups = paragraphs.associate {
            val lines = it.lines().filter(String::isNotBlank)

            lines.first() to lines.drop(1)
        }

        return symbolGroups.mapValues { getValues(it.value) }
    }

    abstract fun getValues(assocLines: List<String>): T
}

class OnYomiParser(rawContent: String) : NewlineGroupParser<List<OnYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyKatakana() }
            .map { OnYomi(it) }
}

class KunYomiParser(
    rawContent: String,
    val annotationMode: KunYomiAnnotationMode = KunYomiAnnotationMode.BracketKunYomiParser
) : NewlineGroupParser<List<KunYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyHiragana() }
            .map(annotationMode::parse)
}

class VocabularyWithSampleSentenceParser(rawContent: String, val vocabAlignmentSequence: String) : NewlineGroupParser<List<Pair<VocabularyItem, SampleSentence?>>>(rawContent) {
    override fun getValues(assocLines: List<String>): List<Pair<VocabularyItem, SampleSentence?>> {
        return assocLines.map {
            val parts = it.split("\t")
            val (fullText, reading, transRaw) = parts.take(3)

            val kanjiParts = fullText.pluckKanji()
            val readingsParts = reading.split(vocabAlignmentSequence)

            val alignedReading = kanjiParts.zip(readingsParts).map { p ->
                if (p.first == p.second) KanaReading(p.first) else KanjiReading(p.first.first(), p.second)
            }

            val translations = transRaw.split(",").map(String::trim)

            val optSample = parts.getOrNull(4)
            val sampleSentence = optSample?.let(::SampleSentence)

            VocabularyItem(alignedReading, translations) to sampleSentence
        }
    }
}
