package com.suushiemaniac.lang.japanese.kanji.parser

import com.suushiemaniac.lang.japanese.kanji.util.allTo
import com.suushiemaniac.lang.japanese.kanji.util.toStringifiedChars
import kotlin.math.ceil
import kotlin.math.roundToInt

abstract class FileParser<T>(val rawContent: String) {
    abstract fun getAssociations(): Map<String, T>
}

class NumericalIndexParser(rawContent: String): FileParser<Int>(rawContent) {
    override fun getAssociations(): Map<String, Int> {
        val lines = rawContent.lines()
        val chars = lines.flatMap { it.toStringifiedChars() }

        return chars.mapIndexed { i, k -> k to i }.toMap()
    }
}

open class PageGroupsParser(rawContent: String, val pagesPerLine: Int = 2): FileParser<Int>(rawContent) {
    override fun getAssociations(): Map<String, Int> {
        val lines = rawContent.lines()

        val lineGroups = lines.mapIndexed { i, ln ->
            val chunkSizeDecimal = ln.length.toFloat() / pagesPerLine
            val chunkSize = ceil(chunkSizeDecimal).roundToInt()

            i to ln.chunked(chunkSize)
        }

        val pairAssociations = lineGroups.flatMap { (i, ks) -> ks allTo i }
        return pairAssociations.toMap()
    }
}

class LessonsParser(rawContent: String): PageGroupsParser(rawContent, 1)