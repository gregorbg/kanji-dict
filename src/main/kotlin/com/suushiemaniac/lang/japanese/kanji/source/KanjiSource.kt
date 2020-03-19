package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.Kanji

interface KanjiSource {
    fun lookupSymbol(kanji: Char): Kanji?

    fun fetchAll(): List<Kanji>
}