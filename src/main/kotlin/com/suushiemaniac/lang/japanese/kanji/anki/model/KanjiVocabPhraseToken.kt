package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.atilika.kuromoji.ipadic.Token
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.TranslationSource
import com.suushiemaniac.lang.japanese.kanji.util.*

data class KanjiVocabPhraseToken(
    private val readingWithSurfaceForm: ReadingWithSurfaceForm,
    val tokenData: Map<String, String>,
    val annotation: String? = null
) : ReadingWithSurfaceForm by readingWithSurfaceForm {
    companion object {
        val LOOKUP_TOKEN_TYPES = setOf("名詞", "動詞", "形容詞")
        const val UNSPECIFIED_SKIP_TOKEN = "*"

        fun from(
            japToken: Token,
            translationSource: TranslationSource,
            kanjiSource: KanjiSource
        ): KanjiVocabPhraseToken {
            val tokenReading = japToken.reading.takeIf { japToken.surface.containsOnlyKatakana() }
                ?: japToken.reading.toHiragana()

            val matchedReading = japToken.surface.alignReadingsWith(tokenReading, kanjiSource)
                .unlessEmpty() ?: listOf(BackupReading(tokenReading, japToken.surface))

            val readingHack = VocabularyItem(matchedReading, emptyList())

            val tokenData = TOKEN_KEYS.zip(japToken.allFeaturesArray).toMap()
                .filterValues { it != UNSPECIFIED_SKIP_TOKEN }

            val vocabData = japToken.takeIf { it.partOfSpeechLevel1 in LOOKUP_TOKEN_TYPES }
                ?.let { translationSource.lookupWord(it.baseForm) }

            val translationString = vocabData?.translations?.unlessEmpty()?.joinToString()

            return KanjiVocabPhraseToken(readingHack, tokenData, translationString)
        }

        internal data class BackupReading(override val reading: String, override val surfaceForm: String) : ReadingWithSurfaceForm
    }
}
