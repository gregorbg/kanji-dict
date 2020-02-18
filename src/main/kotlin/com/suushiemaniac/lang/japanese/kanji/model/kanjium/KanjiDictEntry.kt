package com.suushiemaniac.lang.japanese.kanji.model.kanjium

import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KanjiMinistryList
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KanjiType
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.KunYomi
import com.suushiemaniac.lang.japanese.kanji.model.kanjium.enumeration.OnYomi

data class KanjiDictEntry(
    val kanji: Char,
    val radical: Radical,
    val radicalVariant: Radical?,
    val phoneticCarrier: Char?,
    val idc: String,
    val type: KanjiType?,
    val regularOnYomi: List<OnYomi>,
    val regularKunYomi: List<KunYomi>,
    val onYomi: List<OnYomi>,
    val kunYomi: List<KunYomi>,
    val nanori: List<String>,
    val strokes: Int,
    val list: KanjiMinistryList,
    val educationGrade: Int?,
    val jlpt: Int?,
    val kanken: Int?,
    val frequency: Int?,
    val meaning: List<String>,
    val compactMeaning: List<String>
)
