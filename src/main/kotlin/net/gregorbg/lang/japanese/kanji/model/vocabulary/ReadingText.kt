package net.gregorbg.lang.japanese.kanji.model.vocabulary

import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SentenceLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.TextLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.util.*

data class ReadingText(override val sentences: List<SentenceLevelToken<WordLevelToken>>) :
    TextLevelToken<WordLevelToken> {

    override val delimiterToken = DELIMITER_TOKEN

    override fun withMorphology() = MorphologyText.parse(this.surfaceForm)

    companion object {
        const val SENTENCE_DELIMITER = FULLSTOP_KUTOTEN.toString()
        val DELIMITER_TOKEN = CompoundKanaToken(SENTENCE_DELIMITER)

        fun parse(raw: String): ReadingText {
            val intermediateSentence = SampleSentence.parseWithMorphology(raw)
            val fullTokens = intermediateSentence.tokens

            return fromTokens(fullTokens)
        }

        fun fromTokens(tokens: List<WordLevelToken>): ReadingText {
            val cleanTokens = tokens.flatMap {
                if (it is CompoundKanaToken) {
                    if (SENTENCE_DELIMITER in it.kana) {
                        val (before, after) = it.kana.split(SENTENCE_DELIMITER)
                        val mappedTokens = listOf(
                            CompoundKanaToken(before),
                            DELIMITER_TOKEN,
                            CompoundKanaToken(after)
                        )

                        mappedTokens.filter { t -> t.surfaceForm.isNotBlank() }
                    } else it.singletonList()
                } else it.singletonList()
            }

            val decomposed = cleanTokens.decompose(SENTENCE_DELIMITER) { it.surfaceForm }
            val sentences = decomposed.map { SampleSentence(it) }

            return ReadingText(sentences)
        }
    }
}