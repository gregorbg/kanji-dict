package net.gregorbg.lang.japanese.kanji.model.vocabulary

import com.atilika.kuromoji.ipadic.Tokenizer
import net.gregorbg.lang.japanese.kanji.model.reading.token.*
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.SentenceLevelToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.util.toHiragana

data class SampleSentence<T : WordLevelToken>(override val tokens: List<T>) : SentenceLevelToken<T> {
    companion object {
        private val TOKENIZER = Tokenizer()

        fun parseWithMorphology(raw: String): SampleSentence<MorphologyToken> {
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