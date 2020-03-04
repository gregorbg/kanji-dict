package com.suushiemaniac.lang.japanese.kanji.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.CompositeReading
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm

data class VocabularyItem(override val readingParts: List<ReadingWithSurfaceForm>, val translations: List<String>) : CompositeReading
