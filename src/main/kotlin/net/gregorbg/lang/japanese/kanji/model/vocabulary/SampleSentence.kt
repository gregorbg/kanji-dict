package net.gregorbg.lang.japanese.kanji.model.vocabulary

import com.atilika.kuromoji.ipadic.Tokenizer
import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeWordLevelTokens
import net.gregorbg.lang.japanese.kanji.util.toHiragana

data class SampleSentence(override val tokens: List<MorphologyToken>) : CompositeWordLevelTokens<MorphologyToken> {
    companion object {
        private val TOKENIZER = Tokenizer()

        fun parse(raw: String): SampleSentence {
            val extTokens = TOKENIZER.tokenize(raw)

            val intTokens = extTokens.map {
                val morphData = MorphologicalData.from(it)
                MorphologyToken(it.surface, it.reading.toHiragana(), morphData)
            }

            return SampleSentence(intTokens).also {
                require(it.surfaceForm == raw) { "Sample sentence parse unsuccessful!" }
            }
        }
    }
}