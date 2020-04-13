package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeWordLevelTokens
import com.suushiemaniac.lang.japanese.kanji.util.FULLSTOP_KUTOTEN
import com.suushiemaniac.lang.japanese.kanji.util.decompose

data class MorphologyText(override val sentences: List<SampleSentence>) :
    CompositeWordLevelTokens<MorphologyToken>, ComplexText<MorphologyToken, SampleSentence> {

    override val delimiterToken = DELIMITER_TOKEN

    companion object {
        const val SENTENCE_DELIMITER = FULLSTOP_KUTOTEN.toString()

        private val DELIMITER_TOKEN = SampleSentence.parse(SENTENCE_DELIMITER).tokens.single()

        fun parse(raw: String): MorphologyText {
            val fullTokens = SampleSentence.parse(raw).tokens
            return fromTokens(fullTokens)
        }

        fun fromTokens(tokens: List<MorphologyToken>): MorphologyText {
            val decomposed = tokens.decompose(SENTENCE_DELIMITER) { it.surfaceForm }
            val sentences = decomposed.map { SampleSentence(it) }

            return MorphologyText(sentences)
        }
    }
}