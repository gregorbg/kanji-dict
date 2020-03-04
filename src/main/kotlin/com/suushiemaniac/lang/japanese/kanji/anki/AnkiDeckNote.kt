package com.suushiemaniac.lang.japanese.kanji.anki

interface AnkiDeckNote {
    fun getCSVFacts(): List<String>

    fun getTags(): List<String>

    companion object {
        const val CSV_SEPARATOR = "\t"
    }
}