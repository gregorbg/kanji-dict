package net.gregorbg.lang.japanese.kanji.model.vocabulary

import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import net.gregorbg.lang.japanese.kanji.util.*

data class ReadingText(override val sentences: List<CompositeSymbolTokens<SymbolToken>>) :
    CompositeSymbolTokens<SymbolToken>, ComplexText<SymbolToken> {

    override val delimiterToken = DELIMITER_TOKEN

    override fun withMorphology() = MorphologyText.parse(this.surfaceForm)

    companion object {
        const val SENTENCE_DELIMITER = FULLSTOP_KUTOTEN.toString()
        val DELIMITER_TOKEN = KanaToken(SENTENCE_DELIMITER)

        fun parse(raw: String): ReadingText {
            val intermediateSentence = SampleSentence.parse(raw)
            val fullTokens = intermediateSentence.asSymbols().tokens

            return fromTokens(fullTokens)
        }

        fun fromTokens(tokens: List<SymbolToken>): ReadingText {
            val flatTokens = ConvertedSymbolTokens(tokens).flatten()

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
            val sentences = decomposed.map(::ConvertedSymbolTokens)

            return ReadingText(sentences)
        }
    }
}