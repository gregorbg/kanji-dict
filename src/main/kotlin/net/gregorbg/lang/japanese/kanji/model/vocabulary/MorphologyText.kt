package net.gregorbg.lang.japanese.kanji.model.vocabulary

import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SentenceLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.TextLevelToken
import net.gregorbg.lang.japanese.kanji.util.FULLSTOP_KUTOTEN
import net.gregorbg.lang.japanese.kanji.util.decompose

data class MorphologyText(override val sentences: List<SentenceLevelToken<MorphologyToken>>) : TextLevelToken<MorphologyToken> {
    override val delimiterToken = DELIMITER_TOKEN

    override fun withMorphology() = this

    companion object {
        const val SENTENCE_DELIMITER = FULLSTOP_KUTOTEN.toString()

        private val DELIMITER_TOKEN = SampleSentence.parseWithMorphology(SENTENCE_DELIMITER).tokens.single()

        fun parse(raw: String): MorphologyText {
            val fullTokens = SampleSentence.parseWithMorphology(raw).tokens
            return fromTokens(fullTokens)
        }

        fun fromTokens(tokens: List<MorphologyToken>): MorphologyText {
            val decomposed = tokens.decompose(SENTENCE_DELIMITER) { it.surfaceForm }
            val sentences = decomposed.map { SampleSentence(it + DELIMITER_TOKEN) }

            return MorphologyText(sentences)
        }
    }
}