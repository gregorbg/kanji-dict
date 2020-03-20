package com.suushiemaniac.lang.japanese.kanji.source.workbook

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.reading.KunYomiAnnotationMode
import com.suushiemaniac.lang.japanese.kanji.model.workbook.SimpleKanji
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.KunYomiParser
import com.suushiemaniac.lang.japanese.kanji.source.workbook.parser.OnYomiParser

data class KanjiWorkbookSource(val bookNum: Int) : KanjiSource {
    private val lessonsContent = loadFile(bookNum, LESSONS_FILE_TAG)
    private val readingsContent = loadFile(bookNum, READINGS_FILE_TAG)
    private val samplesContent = loadFile(bookNum, SAMPLES_FILE_TAG)

    val onYomiParser = OnYomiParser(readingsContent)
    val kunYomiParser = KunYomiParser(readingsContent, KUNYOMI_PARSER)

    val onYomiData by lazy { onYomiParser.getAssociations() }
    val kunYomiData by lazy { kunYomiParser.getAssociations() }

    override fun lookupSymbol(kanji: Char): Kanji? {
        val onYomis = onYomiData[kanji.toString()].orEmpty()
        val kunYomis = kunYomiData[kanji.toString()].orEmpty()

        if (onYomis.isNotEmpty() || kunYomis.isNotEmpty()) {
            return SimpleKanji(kanji, onYomis, kunYomis)
        }

        return null
    }

    override fun fetchAll(): List<Kanji> {
        val allKeys = onYomiData.keys + kunYomiData.keys
        return allKeys.mapNotNull { lookupSymbol(it.first()) }
    }

    companion object {
        const val RESOURCE_PACKAGE = "15kanji_workbook"

        const val LESSONS_FILE_TAG = "lessons"
        const val READINGS_FILE_TAG = "readings"
        const val SAMPLES_FILE_TAG = "samples"

        const val KUNYOMI_SEPARATOR = "."

        val RESOURCE_NAMES = listOf("beginner_1", "beginner_2", "intermediate_1", "intermediate_2")

        val KUNYOMI_PARSER = KunYomiAnnotationMode.SeparatorKunYomiParser(KUNYOMI_SEPARATOR)

        fun loadFile(bookNum: Int, fileTag: String): String {
            val resourceBookName = RESOURCE_NAMES[bookNum]
            val resourcePath = "/$RESOURCE_PACKAGE/$resourceBookName/$fileTag.txt"

            return KanjiWorkbookSource::class.java.getResourceAsStream(resourcePath)
                ?.reader()?.use { it.readText() }.orEmpty()
        }
    }
}
