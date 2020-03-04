package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm

data class KanjiVocabPhraseToken(
    private val readingWithSurfaceForm: ReadingWithSurfaceForm,
    val tokenData: Map<String, String>,
    val annotation: String
) : ReadingWithSurfaceForm by readingWithSurfaceForm