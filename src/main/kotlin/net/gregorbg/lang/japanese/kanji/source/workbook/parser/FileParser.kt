package net.gregorbg.lang.japanese.kanji.source.workbook.parser

abstract class FileParser<T>(val rawContent: String) {
    abstract fun getAssociations(): Map<String, T>

    val nonBlankLines get() = rawContent.lines().filter { it.isNotBlank() }
}
