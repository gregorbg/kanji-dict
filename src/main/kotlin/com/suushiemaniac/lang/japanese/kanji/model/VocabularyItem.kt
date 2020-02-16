package com.suushiemaniac.lang.japanese.kanji.model

data class VocabularyItem(val fullText: String, val reading: String, val translations: List<String>, val sampleSentence: String? = null)
