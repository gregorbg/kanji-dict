package com.suushiemaniac.lang.japanese.kanji.anki

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

object AnkiExporter {
    const val CSV_SEPARATOR = "\t"
    const val CSV_NEWLINE = "\n"

    val JSON = Json(JsonConfiguration.Stable)

    fun makeCSV(cards: List<AnkiDeckNote>): String {
        val csvLines = cards.map {
            val csvLineContent = it.getCSVFacts() + it.getTags()
            csvLineContent.joinToString(CSV_SEPARATOR)
        }

        return csvLines.joinToString(CSV_NEWLINE)
    }
}