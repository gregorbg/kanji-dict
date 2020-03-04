package com.suushiemaniac.lang.japanese.kanji.model

import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.OnYomi

interface Kanji {
    val kanji: Char
    val onYomi: List<OnYomi>
    val kunYomi: List<KunYomi>
}