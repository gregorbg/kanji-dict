package com.suushiemaniac.lang.japanese.kanji.model.kanjium

data class Elements(
    val kanji: Char,
    val idc: String,
    val elements: List<String>,
    val extraElements: List<String>,
    val kanjiParts: List<String>,
    val partOf: List<Char>,
    val compactMeaning: String
)