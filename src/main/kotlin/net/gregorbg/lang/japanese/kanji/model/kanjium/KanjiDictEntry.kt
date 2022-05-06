package net.gregorbg.lang.japanese.kanji.model.kanjium

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.kanjium.enumeration.KanjiMinistryList
import net.gregorbg.lang.japanese.kanji.model.kanjium.enumeration.KanjiType
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiKunYomi
import net.gregorbg.lang.japanese.kanji.model.reading.annotation.KanjiOnYomi
import kotlinx.serialization.Serializable

@Serializable
data class KanjiDictEntry(
    override val kanji: Char,
    val radical: Radical,
    val radicalVariant: Radical?,
    val phoneticCarrier: Char?,
    val idc: String,
    val type: KanjiType?,
    val regularOnYomi: List<KanjiOnYomi>,
    val regularKunYomi: List<KanjiKunYomi>,
    override val onYomi: List<KanjiOnYomi>,
    override val kunYomi: List<KanjiKunYomi>,
    val nanori: List<String>,
    val strokes: Int,
    val list: KanjiMinistryList,
    val educationGrade: Int?,
    val jlpt: Int?,
    val kanken: Int?,
    val frequency: Int?,
    val meaning: List<String>,
    val compactMeaning: List<String>
) : Kanji
