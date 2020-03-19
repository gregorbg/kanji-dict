package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.VocabularyItem

interface TranslationSource {
    fun lookupWord(raw: String): VocabularyItem?
}
