package com.suushiemaniac.lang.japanese.kanji.util

import com.mariten.kanatools.KanaConverter

fun String.toStringifiedChars() = toCharArray().map { it.toString().intern() }

fun String.japaneseSymbolsToASCII() = KanaConverter.convertKana(this, KanaConverter.OP_ZEN_ASCII_TO_HAN_ASCII)

fun String.splitAndTrim(delimiter: String) = split(delimiter).map(String::trim)

val String.words get() = splitAndTrim(" ")
val String.commaTokens get() = splitAndTrim(",")

const val EMPTY_STRING = ""