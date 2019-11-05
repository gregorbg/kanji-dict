package com.suushiemaniac.lang.japanese.kanji.model

data class SampleContent(val id: Int, val referenceId: Int, val word: List<ReadingToken>, val translation: String, val sourceId: Int)

data class ReadingToken(val symbols: String, val reading: String)
