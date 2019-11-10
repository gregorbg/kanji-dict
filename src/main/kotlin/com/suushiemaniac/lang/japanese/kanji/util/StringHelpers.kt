package com.suushiemaniac.lang.japanese.kanji.util

fun String.toStringifiedChars() = toCharArray().map { it.toString().intern() }