package com.suushiemaniac.lang.japanese.kanji.model.kanjium

data class Radical(
    val radical: Char,
    val parentRadical: Radical?,
    val number: Int,
    val strokes: Int,
    val names: List<String>,
    val meanings: List<String>,
    val notes: String
)