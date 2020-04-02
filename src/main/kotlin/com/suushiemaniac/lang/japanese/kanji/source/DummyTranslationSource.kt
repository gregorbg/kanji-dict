package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem
import com.suushiemaniac.lang.japanese.kanji.model.reading.KanaReading

object DummyTranslationSource : TranslationSource {
    override fun lookupWord(raw: String): VocabularyItem? {
        return VocabularyItem(listOf(KanaReading(raw)), listOf(DUMMY_TRANSLATION))
    }

    const val DUMMY_TRANSLATION = "TBD"
}
