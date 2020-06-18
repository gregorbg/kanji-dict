package com.suushiemaniac.lang.japanese.kanji.model.kanjivg

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
sealed class SvgElement {
    abstract val id: String

    abstract fun withRadicalColor(): SvgElement

    @Serializable
    @XmlSerialName("g", KanjiVG.SVG_NAMESPACE, KanjiVG.SVG_PREFIX)
    data class Group(
        override val id: String,
        val style: String? = null,
        @XmlSerialName("element", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val element: String? = null,
        @XmlSerialName("original", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val original: String? = null,
        @XmlSerialName("position", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val position: Position? = null,
        @XmlSerialName("variant", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val variant: Boolean = false,
        @XmlSerialName("partial", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val partial: Boolean = false,
        @XmlSerialName("part", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val part: Int? = null,
        @XmlSerialName("number", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val number: Int? = null,
        @XmlSerialName("radical", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val radical: Radical? = null,
        @XmlSerialName("phon", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val phon: String? = null,
        @XmlSerialName(
            "tradForm",
            KanjiVG.KANJIVG_NAMESPACE,
            KanjiVG.KANJIVG_PREFIX
        ) val traditionalForm: Boolean = false,
        @XmlSerialName(
            "radicalForm",
            KanjiVG.KANJIVG_NAMESPACE,
            KanjiVG.KANJIVG_PREFIX
        ) val radicalForm: Boolean = false,
        val elements: List<SvgElement>
    ) : SvgElement() {
        override fun withRadicalColor(): SvgElement {
            val colorStyle =
                if (radical == null) style else listOfNotNull(style, "stroke:${radical.color}").joinToString(";")

            val coloredElements = elements.map { it.withRadicalColor() }
            return copy(style = colorStyle, elements = coloredElements)
        }
    }

    @Serializable
    @XmlSerialName("path", KanjiVG.SVG_NAMESPACE, KanjiVG.SVG_PREFIX)
    data class Path(
        override val id: String,
        val d: String,
        @XmlSerialName("type", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val type: String? = null
    ) : SvgElement() {
        override fun withRadicalColor(): SvgElement {
            return this
        }
    }
}