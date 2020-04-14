package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.suushiemaniac.lang.japanese.kanji.util.associateWithNotNull

data class KanjiVocabPhrase(val phraseSegments: List<KanjiVocabPhraseToken>) {
    fun getLiterals() = phraseSegments.map { it.surfaceForm }

    fun getReadings() = keyedIfExisting {
        it.takeIf { _ -> it.reading != it.surfaceForm }?.token?.asSymbols()?.asFurigana(RubyFuriganaFormatter)
    }

    fun getTokenData() = keyedIfExisting { it.tokenData?.exportData() }
    fun getAnnotations() = keyedIfExisting { it.annotation }

    private fun <T> keyedIfExisting(mapping: (KanjiVocabPhraseToken) -> T?): Map<String, T> {
        return phraseSegments.associateWithNotNull(mapping).mapKeys { it.key.surfaceForm }
    }
}
