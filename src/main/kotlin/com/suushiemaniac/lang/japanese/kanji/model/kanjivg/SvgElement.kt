package com.suushiemaniac.lang.japanese.kanji.model.kanjivg

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
sealed class SvgElement {
    abstract val id: String

    abstract fun withRadicalColor(level: Int = 0): SvgElement
    abstract fun withRadicalBox(): SvgElement

    abstract fun withIdSuffix(suffix: String): SvgElement

    abstract fun withStyle(addStyle: String): SvgElement
    abstract fun withTransform(addTransform: String): SvgElement

    @Serializable
    @XmlSerialName("g", KanjiVG.SVG_NAMESPACE, KanjiVG.SVG_PREFIX)
    data class Group(
        override val id: String,
        val style: String? = null,
        val transform: String? = null,
        @XmlSerialName("element", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val element: String? = null,
        @XmlSerialName("original", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val original: String? = null,
        @XmlSerialName(
            "position",
            KanjiVG.KANJIVG_NAMESPACE,
            KanjiVG.KANJIVG_PREFIX
        ) @XmlElement(false) val position: Position? = null,
        @XmlSerialName("variant", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val variant: Boolean = false,
        @XmlSerialName("partial", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val partial: Boolean = false,
        @XmlSerialName("part", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val part: Int? = null,
        @XmlSerialName("number", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val number: Int? = null,
        @XmlSerialName(
            "radical",
            KanjiVG.KANJIVG_NAMESPACE,
            KanjiVG.KANJIVG_PREFIX
        ) @XmlElement(false) val radical: Radical? = null,
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
        override fun withRadicalColor(level: Int): SvgElement {
            if (radical == null) {
                val coloredElements = elements
                    .filter { level == 0 || it is Group }
                    .map { it.withRadicalColor(level) }

                return copy(elements = coloredElements)
            }

            val nestedRadicals = elements
                .filterIsInstance<Group>()
                .map { it.withIdSuffix("nested") }
                .map { it.withTransform(makeTransformStyle(level + 1)) }
                .map { it.withRadicalColor(level + 1) }

            val coloredChildren = nestedRadicals + elements

            return withStyle(makeColorStyle(radical.color))
                .copy(elements = coloredChildren)
        }

        override fun withRadicalBox(): SvgElement {
            val coloredElements = elements.map { it.withRadicalBox() }

            if (radical == null) {
                return copy(elements = coloredElements)
            }

            return withStyle(makeBoxStyle(radical.color))
                .copy(elements = coloredElements)
        }

        override fun withIdSuffix(suffix: String): Group {
            val suffixedChildren = elements.map { it.withIdSuffix(suffix) }
            return copy(id = "$id-$suffix", elements = suffixedChildren)
        }

        override fun withStyle(addStyle: String): Group {
            val newStyle = listOfNotNull(style, addStyle).joinToString(";")
            return copy(style = newStyle)
        }

        override fun withTransform(addTransform: String): Group {
            val newTransform = listOfNotNull(transform, addTransform).joinToString(" ")
            return copy(transform = newTransform)
        }

        companion object {
            const val TRANSFORM_OFFSET = 4.2

            private fun makeColorStyle(color: String) = "stroke:$color"
            private fun makeBoxStyle(color: String) = "outline:solid 2px $color"
            private fun makeTransformStyle(level: Int) =
                "translate(${level * TRANSFORM_OFFSET},${level * TRANSFORM_OFFSET})"
        }
    }

    @Serializable
    @XmlSerialName("path", KanjiVG.SVG_NAMESPACE, KanjiVG.SVG_PREFIX)
    data class Path(
        override val id: String,
        val d: String,
        @XmlSerialName("type", KanjiVG.KANJIVG_NAMESPACE, KanjiVG.KANJIVG_PREFIX) val type: String? = null
    ) : SvgElement() {
        override fun withRadicalColor(level: Int) = this
        override fun withRadicalBox() = this

        override fun withIdSuffix(suffix: String) = copy(id = "$id-$suffix")
        override fun withStyle(addStyle: String) = this
        override fun withTransform(addTransform: String) = this
    }
}