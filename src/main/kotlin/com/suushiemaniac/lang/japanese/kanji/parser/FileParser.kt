package com.suushiemaniac.lang.japanese.kanji.parser

abstract class FileParser<T>(val rawContent: String) {
    abstract fun getAssociations(): Map<String, T>

    val nonBlankLines get() = rawContent.lines().filter { it.isNotBlank() }
}