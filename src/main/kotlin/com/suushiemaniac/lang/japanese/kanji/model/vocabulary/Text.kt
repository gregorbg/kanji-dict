package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeMorphologyTokens
import com.suushiemaniac.lang.japanese.kanji.util.FULLSTOP_KUTOTEN
import com.suushiemaniac.lang.japanese.kanji.util.decompose
import com.suushiemaniac.lang.japanese.kanji.util.interlace

data class Text(val sentences: List<SampleSentence>) : CompositeMorphologyTokens {
    override val tokens: List<MorphologyToken>
        get() = this.sentences.map { it.tokens }.interlace(DELIMITER_TOKEN).toList()

    companion object {
        const val SENTENCE_DELIMITER = FULLSTOP_KUTOTEN.toString()
        val DELIMITER_TOKEN = SampleSentence.parse(SENTENCE_DELIMITER).tokens.single()

        fun parse(raw: String): Text {
            val fullTokens = SampleSentence.parse(raw).tokens
            return fromTokens(fullTokens)
        }

        fun fromTokens(tokens: List<MorphologyToken>): Text {
            val decomposed = tokens.decompose(SENTENCE_DELIMITER) { it.surfaceForm }
            val sentences = decomposed.map { SampleSentence(it) }

            return Text(sentences)
        }
    }
}