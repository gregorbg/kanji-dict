package com.suushiemaniac.lang.japanese.kanji.anki

import kotlinx.serialization.json.Json

object AnkiExporter {
    const val CSV_SEPARATOR = AnkiDeckNote.CSV_SEPARATOR
    const val CSV_NEWLINE = "\n"

    val JSON = Json.Default

    fun makeCSV(cards: List<AnkiDeckNote>): String {
        val csvLines = cards.map {
            val csvLineContent = it.getCSVFacts() + it.getTags().joinToString(" ")
            csvLineContent.joinToString(CSV_SEPARATOR)
        }

        return csvLines.joinToString(CSV_NEWLINE)
    }
}