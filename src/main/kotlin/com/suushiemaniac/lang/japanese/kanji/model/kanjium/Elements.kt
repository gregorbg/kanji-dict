package com.suushiemaniac.lang.japanese.kanji.model.kanjium

import com.suushiemaniac.lang.japanese.kanji.model.KanjiElements

data class Elements(
    val kanji: Char,
    override val idc: String,
    val elements: List<String>,
    val extraElements: List<String>,
    val kanjiParts: List<String>,
    val partOf: List<Char>,
    val compactMeaning: String
) : KanjiElements {
    override val components
        get() = kanjiParts.mapNotNull(String::singleOrNull).toSet()
}