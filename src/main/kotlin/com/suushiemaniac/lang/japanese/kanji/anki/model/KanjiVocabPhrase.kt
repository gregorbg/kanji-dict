package com.suushiemaniac.lang.japanese.kanji.anki.model

data class KanjiVocabPhrase(val phraseSegments: List<KanjiVocabPhraseToken>) {
    fun getLiterals() = phraseSegments.map { it.surfaceForm }
    fun getReadings() = keyedIfExisting { it.reading.takeIf { r -> r != it.surfaceForm } }
    fun getTokenData() = keyedIfExisting { it.tokenData.takeUnless(Map<String, String>::isEmpty) }
    fun getAnnotations() = keyedIfExisting { it.annotation }

    private fun <T> keyedIfExisting(mapping: (KanjiVocabPhraseToken) -> T?): Map<String, T> {
        return phraseSegments.mapNotNull { t -> mapping(t)?.let { t.surfaceForm to it } }.toMap()
    }
}
