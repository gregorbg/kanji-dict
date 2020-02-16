package com.suushiemaniac.lang.japanese.kanji.model

data class KanjiCharacter(val kanji: Char, val strokeCount: Int, val radical: KanjiRadical?)

data class KanjiRadical(val radicalSymbol: Char, val name: String)