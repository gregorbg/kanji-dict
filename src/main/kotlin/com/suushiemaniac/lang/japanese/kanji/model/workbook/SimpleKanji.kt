package com.suushiemaniac.lang.japanese.kanji.model.workbook

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.OnYomi

data class SimpleKanji(
    override val kanji: Char,
    override val onYomi: List<OnYomi>,
    override val kunYomi: List<KunYomi>
) : Kanji