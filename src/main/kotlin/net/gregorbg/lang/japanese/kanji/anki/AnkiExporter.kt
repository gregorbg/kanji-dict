package net.gregorbg.lang.japanese.kanji.anki

import kotlinx.serialization.json.Json

object AnkiExporter {
    const val CSV_SEPARATOR = net.gregorbg.lang.japanese.kanji.anki.AnkiDeckNote.Companion.CSV_SEPARATOR
    const val CSV_NEWLINE = "\n"

    val JSON = Json.Default

    fun makeCSV(cards: List<net.gregorbg.lang.japanese.kanji.anki.AnkiDeckNote>): String {
        val csvLines = cards.map {
            val csvLineContent = it.getCSVFacts() + it.getTags().joinToString(" ")
            csvLineContent.joinToString(net.gregorbg.lang.japanese.kanji.anki.AnkiExporter.CSV_SEPARATOR)
        }

        return csvLines.joinToString(net.gregorbg.lang.japanese.kanji.anki.AnkiExporter.CSV_NEWLINE)
    }
}