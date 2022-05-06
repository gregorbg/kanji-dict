package net.gregorbg.lang.japanese.kanji.util

import com.mariten.kanatools.KanaConverter

fun String.toStringifiedChars() = toCharArray().map { it.toString().intern() }

fun String.japaneseSymbolsToASCII() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_ASCII_TO_HAN_ASCII)

fun String.splitAndTrim(delimiter: String) = split(delimiter).map(String::trim)

fun String.prependIndent(level: Int, mode: IndentMode) = prependIndent(mode.indentUnit.repeat(level))
fun String.trimBlankLines() = lineSequence().map { it.takeUnless(String::isBlank).orEmpty() }.joinToString("\n")

fun String.ensureEndsWith(char: Char) = takeIf { it.endsWith(char) } ?: "$this$char"

val String.words get() = splitAndTrim(" ")
val String.commaTokens get() = splitAndTrim(",")

const val EMPTY_STRING = ""

enum class IndentMode(val indentSymbol: Char, val multiplier: Int = 1) {
    TAB('\t'),
    SPACE(' ', 4);

    val indentUnit get() = indentSymbol.toString().repeat(multiplier)
}