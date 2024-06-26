package net.gregorbg.lang.japanese.kanji.source.workbook.parser

import net.gregorbg.lang.japanese.kanji.util.allTo
import net.gregorbg.lang.japanese.kanji.util.toStringifiedChars
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

class NumericalIndexParser(rawContent: String) : FileParser<Int>(rawContent) {
    override fun getAssociations(): Map<String, Int> {
        val chars = nonBlankLines.flatMap { it.toStringifiedChars() }

        return chars.mapIndexed { i, kj -> kj to i }.toMap()
    }
}

open class PageGroupsParser(
    rawContent: String,
    private val pagesPerLine: Int = 2,
    private val fixShortLines: Boolean = true
) : FileParser<Int>(rawContent) {
    override fun getAssociations(): Map<String, Int> {
        val lines = this.nonBlankLines

        val avgLineLength = lines.map { it.length }.average().roundToInt()

        val lineGroups = lines.flatMap { ln ->
            val chunkSizeDecimal = ln.length.toFloat() / pagesPerLine
            val chunkSize = ceil(chunkSizeDecimal).roundToInt()

            val usedChunkSize = chunkSize.takeUnless { fixShortLines }
                ?: max(avgLineLength, chunkSize)

            ln.chunked(usedChunkSize)
        }

        return lineGroups.flatMapIndexed { i, ks -> ks.toStringifiedChars() allTo i }.toMap()
    }
}

class LessonsParser(rawContent: String) : PageGroupsParser(rawContent, 1)