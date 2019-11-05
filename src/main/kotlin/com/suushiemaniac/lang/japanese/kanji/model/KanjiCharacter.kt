package com.suushiemaniac.lang.japanese.kanji.model

data class KanjiCharacter(val id: Int, val kanji: Char, val strokeCount: Int, val radical: KanjiRadical)

data class KanjiRadical(val id: Int, val radicalSymbol: Char, val name: String)