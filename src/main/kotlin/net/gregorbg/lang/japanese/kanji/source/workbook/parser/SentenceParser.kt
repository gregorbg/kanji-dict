package net.gregorbg.lang.japanese.kanji.source.workbook.parser

import net.gregorbg.lang.japanese.kanji.model.vocabulary.SampleSentence
import net.gregorbg.lang.japanese.kanji.model.vocabulary.VocabularyItem
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi
import net.gregorbg.lang.japanese.kanji.model.reading.token.KanaToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.KanjiToken
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KunYomiAnnotationMode
import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologyToken
import net.gregorbg.lang.japanese.kanji.model.vocabulary.Translation
import net.gregorbg.lang.japanese.kanji.util.*

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

class OnYomiParser(rawContent: String) : NewlineGroupParser<List<KanjiOnYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyKatakana() }
            .map { KanjiOnYomi(it) }
}

class KunYomiParser(
    rawContent: String,
    val kunYomiParser: KunYomiAnnotationMode = KunYomiAnnotationMode.BracketKunYomiParser
) : NewlineGroupParser<List<KanjiKunYomi>>(rawContent) {
    override fun getValues(assocLines: List<String>) =
        assocLines.filter { it.containsOnlyHiraganaOrAnnotations(kunYomiParser) }
            .map(kunYomiParser::parse)
}

class VocabularyWithSampleSentenceParser(rawContent: String, val vocabAlignmentSequence: String) :
    NewlineGroupParser<List<Triple<VocabularyItem, Translation, SampleSentence<MorphologyToken>?>>>(rawContent) {
    override fun getValues(assocLines: List<String>): List<Triple<VocabularyItem, Translation, SampleSentence<MorphologyToken>?>> {
        return assocLines.map {
            val parts = it.split("\t")
            val (fullText, reading, transRaw) = parts.take(3)

            val kanjiParts = fullText.pluckKanji()
            val readingsParts = reading.split(vocabAlignmentSequence)

            require(kanjiParts.size == readingsParts.size) { "Incorrect workbook alignment for $fullText" }

            val alignedReading = kanjiParts.zip(readingsParts).flatMap { p ->
                val cleanReading = p.second.cleanRendakuAnnotations()
                val baseReading = p.second.normalizeRendakuAnnotations()

                if (p.first == cleanReading) KanaToken.fromWord(p.first) else
                    KanjiToken(p.first.single(), cleanReading, baseReading).singletonList()
            }

            val (realignedReading, vocabModifiers) = alignedReading.guessVocabModifiersAndReAlign()
            val vocabItem = VocabularyItem(realignedReading, vocabModifiers)

            val translationStrings = transRaw.commaTokens
            val translation = Translation(translationStrings.first(), translationStrings.drop(1))

            val sampleSentence = parts.getOrNull(3)?.let(SampleSentence.Companion::parseWithMorphology)

            Triple(vocabItem, translation, sampleSentence)
        }
    }

    companion object {
        const val RENDAKU_ANNOTATION_SYMBOL = '*'
        const val RENDAKU_EXCEPTIONAL_READING_SYMBOL = '/'

        private fun String.cleanRendakuAnnotations(): String {
            if (RENDAKU_EXCEPTIONAL_READING_SYMBOL in this) {
                return this.split(RENDAKU_EXCEPTIONAL_READING_SYMBOL).first()
            }

            return this.trimStart(RENDAKU_ANNOTATION_SYMBOL).split(RENDAKU_ANNOTATION_SYMBOL).first()
        }

        private fun String.normalizeRendakuAnnotations(): String? {
            if (RENDAKU_EXCEPTIONAL_READING_SYMBOL in this) {
                return this.split(RENDAKU_EXCEPTIONAL_READING_SYMBOL).last()
            }

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
