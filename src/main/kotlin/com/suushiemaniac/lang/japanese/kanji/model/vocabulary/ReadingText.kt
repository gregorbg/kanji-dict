package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.*
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.compose.CompositeReadingTokens
import com.suushiemaniac.lang.japanese.kanji.util.*

data class ReadingText(override val sentences: List<CompositeReadingTokens<ReadingToken>>) :
    CompositeReadingTokens<ReadingToken>, ComplexText<ReadingToken, CompositeReadingTokens<ReadingToken>> {

    override val delimiterToken = DELIMITER_TOKEN

    companion object {
        const val SENTENCE_DELIMITER = FULLSTOP_KUTOTEN.toString()
        val DELIMITER_TOKEN = KanaToken(SENTENCE_DELIMITER)

        fun parse(raw: String): ReadingText {
            val intermediateSentence = SampleSentence.parse(raw)
            val fullTokens = intermediateSentence.toReadings().tokens

            return fromTokens(fullTokens)
        }

        fun fromTokens(tokens: List<ReadingToken>): ReadingText {
            val flatTokens = ConvertedReadingTokens(tokens).flatten()

            val cleanTokens = flatTokens.flatMap {
                if (it is KanaToken) {
                    if (SENTENCE_DELIMITER in it.kana) {
                        val (before, after) = it.kana.split(SENTENCE_DELIMITER)
                        val mappedTokens = listOf(KanaToken(before), DELIMITER_TOKEN, KanaToken(after))

                        mappedTokens.filter { t -> t.surfaceForm.isNotBlank() }
                    } else it.singletonList()
                } else it.singletonList()
            }

            val decomposed = cleanTokens.decompose(SENTENCE_DELIMITER) { it.surfaceForm }
            val sentences = decomposed.map { ConvertedReadingTokens(it + DELIMITER_TOKEN) }

            return ReadingText(sentences)
        }
    }
}