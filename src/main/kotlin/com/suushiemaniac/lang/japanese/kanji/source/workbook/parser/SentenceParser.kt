package com.suushiemaniac.lang.japanese.kanji.source.workbook.parser

import com.suushiemaniac.lang.japanese.kanji.model.SampleSentence
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.type.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.type.OnYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanaReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanjiReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.type.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.util.*

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
    val kunYomiParser: KunYomiAnnotationMode = KunYomiAnnotationMode.BracketKunYomiParser
) : NewlineGroupParser<List<KunYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyHiraganaOrAnnotations(kunYomiParser) }
            .map(kunYomiParser::parse)
}

class VocabularyWithSampleSentenceParser(rawContent: String, val vocabAlignmentSequence: String) :
    NewlineGroupParser<List<Pair<VocabularyItem, SampleSentence?>>>(rawContent) {
    override fun getValues(assocLines: List<String>): List<Pair<VocabularyItem, SampleSentence?>> {
        return assocLines.map {
            val parts = it.split("\t")
            val (fullText, reading, transRaw) = parts.take(3)

            val kanjiParts = fullText.pluckKanji()
            val readingsParts = reading.split(vocabAlignmentSequence)

            require(kanjiParts.size == readingsParts.size) { "Incorrect workbook alignment for $fullText" }

            val alignedReading = kanjiParts.zip(readingsParts).map { p ->
                val cleanReading = p.second.cleanRendakuAnnotations()
                val baseReading = p.second.normalizeRendakuAnnotations()

                if (p.first == cleanReading) KanaReading(p.first) else
                    KanjiReading(p.first.first(), cleanReading, baseReading)
            }

            val sampleSentence = parts.getOrNull(3)?.let(::SampleSentence)
            VocabularyItem(alignedReading, transRaw.words) to sampleSentence
        }
    }

    companion object {
        const val RENDAKU_ANNOTATION_SYMBOL = '*'

        private fun String.cleanRendakuAnnotations() =
            this.trimStart(RENDAKU_ANNOTATION_SYMBOL).split(RENDAKU_ANNOTATION_SYMBOL).first()

        private fun String.normalizeRendakuAnnotations(): String? {
            if (this.startsWith(RENDAKU_ANNOTATION_SYMBOL)) {
                val cleaned = this.drop(1).take(1).toKatakanaNoAccents().toHiragana() + this.drop(2)
                return cleaned.normalizeRendakuAnnotations() ?: cleaned
            }

            if (RENDAKU_ANNOTATION_SYMBOL in this) {
                val (core, replacement) = this.split(RENDAKU_ANNOTATION_SYMBOL)
                val cleaned = core.dropLast(1) + replacement.toHiragana()

                return cleaned.normalizeRendakuAnnotations() ?: cleaned
            }

            return null
        }
    }
}
