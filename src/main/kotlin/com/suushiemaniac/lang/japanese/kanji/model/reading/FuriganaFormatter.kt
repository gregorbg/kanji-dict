package com.suushiemaniac.lang.japanese.kanji.model.reading

interface FuriganaFormatter {
    fun format(reading: ReadingWithSurfaceForm): String
}