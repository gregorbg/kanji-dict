package com.suushiemaniac.lang.japanese.kanji.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi

interface Kanji {
    val kanji: Char
    val onYomi: List<KanjiOnYomi>
    val kunYomi: List<KanjiKunYomi>
}