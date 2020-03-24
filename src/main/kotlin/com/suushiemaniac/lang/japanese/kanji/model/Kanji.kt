package com.suushiemaniac.lang.japanese.kanji.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.type.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.reading.type.OnYomi

interface Kanji {
    val kanji: Char
    val onYomi: List<OnYomi>
    val kunYomi: List<KunYomi>
}