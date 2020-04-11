package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.token.MorphologyToken
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource

data class KanjiVocabPhraseToken(
    private val token: MorphologyToken,
    val annotation: String? = null
) : TokenWithSurfaceForm by token {
    val tokenData: Map<String, String>
        get() = token.morphology

    companion object {
        val LOOKUP_TOKEN_TYPES = setOf("名詞", "動詞", "形容詞")

        fun from(
            japToken: MorphologyToken,
            translationSource: TranslationSource
        ): KanjiVocabPhraseToken {
            val vocabData = japToken.takeIf { it.morphology["POS-1"] in LOOKUP_TOKEN_TYPES }
                ?.let { translationSource.getTranslationFor(it) }

            val translationString = vocabData?.mainTranslation
            return KanjiVocabPhraseToken(japToken, translationString)
        }
    }
}
