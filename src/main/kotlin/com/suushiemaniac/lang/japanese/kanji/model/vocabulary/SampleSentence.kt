package com.suushiemaniac.lang.japanese.kanji.model.vocabulary

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.CompositeTokens
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.MorphologyToken
import com.suushiemaniac.lang.japanese.kanji.util.TOKEN_KEYS
import com.suushiemaniac.lang.japanese.kanji.util.UNSPECIFIED_SKIP_TOKEN
import com.suushiemaniac.lang.japanese.kanji.util.toHiragana
import com.suushiemaniac.lang.japanese.kanji.util.tokenizeJapanese

data class SampleSentence(override val tokens: List<MorphologyToken>) : CompositeTokens<MorphologyToken> {
    companion object {
        fun parse(raw: String): SampleSentence {
            val extTokens = raw.tokenizeJapanese()

            val intTokens = extTokens.map {
                val featureMap = TOKEN_KEYS.zip(it.allFeaturesArray).toMap()
                    .filterKeys { k -> k != UNSPECIFIED_SKIP_TOKEN }

                MorphologyToken(it.reading.toHiragana(), it.surface, featureMap)
            }

            return SampleSentence(intTokens)
        }
    }
}