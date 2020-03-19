package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.Kanji

interface KanjiSource {
    operator fun get(kanji: Char): Kanji
}