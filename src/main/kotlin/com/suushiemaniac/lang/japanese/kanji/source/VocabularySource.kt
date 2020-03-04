package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem

interface VocabularySource {
    fun getVocabularyItemsFor(kanji: Kanji): List<VocabularyItem>

    fun lookupWord(raw: String): VocabularyItem?
}
