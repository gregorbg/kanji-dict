package com.suushiemaniac.lang.japanese.kanji.model.kanjivg

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("svg", KanjiVG.SVG_NAMESPACE, KanjiVG.SVG_PREFIX)
data class KanjiVG(
    val width: Int,
    val height: Int,
    val viewBox: String,
    val elements: List<SvgElement>
) {
    fun withRadicalColors(): KanjiVG {
        val coloredElements = elements.map { it.withRadicalColor() }

        return copy(elements = coloredElements)
    }

    companion object {
        const val SVG_NAMESPACE = "http://www.w3.org/2000/svg"
        const val SVG_PREFIX = "" // on purpose

        const val KANJIVG_NAMESPACE = "" // HACK
        const val KANJIVG_PREFIX = "kvg"
    }
}