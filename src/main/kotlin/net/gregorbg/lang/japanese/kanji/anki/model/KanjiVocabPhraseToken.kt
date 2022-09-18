package net.gregorbg.lang.japanese.kanji.anki.model

import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologicalData
import net.gregorbg.lang.japanese.kanji.model.reading.token.MorphologyToken
import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken
import net.gregorbg.lang.japanese.kanji.source.TranslationSource

data class KanjiVocabPhraseToken(
    val token: MorphologyToken,
    val annotation: String? = null
) : WordLevelToken by token {
    val tokenData: MorphologicalData?
        get() = token.morphology

    companion object {
        val LOOKUP_TOKEN_TYPES = setOf("名詞", "動詞", "形容詞")

        fun from(
            japToken: MorphologyToken,
            translationSource: TranslationSource
        ): KanjiVocabPhraseToken {
            val vocabData = japToken.takeIf { it.morphology?.posLevels?.firstOrNull() in LOOKUP_TOKEN_TYPES }
                ?.let { translationSource.getTranslationFor(it) }

            val translationString = vocabData?.mainTranslation
            return KanjiVocabPhraseToken(japToken, translationString)
        }
    }
}
