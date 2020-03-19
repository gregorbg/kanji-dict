package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.atilika.kuromoji.ipadic.Token
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.VocabularySource
import com.suushiemaniac.lang.japanese.kanji.util.TOKEN_KEYS
import com.suushiemaniac.lang.japanese.kanji.util.alignReadingsWith
import com.suushiemaniac.lang.japanese.kanji.util.containsOnlyKatakana
import com.suushiemaniac.lang.japanese.kanji.util.toHiragana

data class KanjiVocabPhraseToken(
    private val readingWithSurfaceForm: ReadingWithSurfaceForm,
    val tokenData: Map<String, String>,
    val annotation: String? = null
) : ReadingWithSurfaceForm by readingWithSurfaceForm {
    companion object {
        val LOOKUP_TOKEN_TYPES = setOf("名詞", "動詞", "形容詞")

        fun from(japToken: Token, translationSource: VocabularySource, kanjiSource: KanjiSource): KanjiVocabPhraseToken {
            val tokenReading = japToken.reading.takeIf { japToken.surface.containsOnlyKatakana() }
                ?: japToken.reading.toHiragana()

            val matchedReading = japToken.surface.alignReadingsWith(tokenReading, kanjiSource)
            val readingHack = VocabularyItem(matchedReading, emptyList())

            val tokenData = TOKEN_KEYS.zip(japToken.allFeaturesArray).toMap()

            val vocabData = japToken.takeIf { it.partOfSpeechLevel1 in LOOKUP_TOKEN_TYPES }
                ?.let { translationSource.lookupWord(it.baseForm) }

            val translationString = vocabData?.translations?.joinToString().orEmpty()

            return KanjiVocabPhraseToken(readingHack, tokenData, translationString)
        }
    }
}
