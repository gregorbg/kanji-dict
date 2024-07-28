package net.gregorbg.lang.japanese.kanji.model.kanjivg

import net.gregorbg.lang.japanese.kanji.model.kanjivg.enumeration.ZoomAndPan
import net.gregorbg.lang.japanese.kanji.model.kanjivg.enumeration.ZoomAndPan.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.io.File

@Serializable
@XmlSerialName("svg", KanjiVG.SVG_NAMESPACE, KanjiVG.SVG_PREFIX)
data class KanjiVG(
    val width: Int,
    val height: Int,
    val viewBox: String,
    val preserveAspectRatio: String = SVG_PRESERVE_ASPECT_RATIO_DEFAULT,
    @XmlElement(false) val zoomAndPan: ZoomAndPan = MAGNIFY,
    val version: String = SVG_VERSION_DEFAULT,
    val contentScriptType: String = SVG_CONTENT_SCRIPT_TYPE_DEFAULT,
    val contentStyleType: String = SVG_CONTENT_STYLE_TYPE_DEFAULT,
    val elements: List<SvgElement>
) {
    fun withColoredRadicals(): KanjiVG {
        val coloredElements = elements.map { it.withRadicalColor() }
        return copy(elements = coloredElements)
    }

    fun withBoxedRadicals(): KanjiVG {
        val boxedElements = elements.map { it.withRadicalBox() }
        return copy(elements = boxedElements)
    }

    fun withoutStrokeNumbers(): KanjiVG {
        val unnumberedElements = elements.map { it.withoutNumberedStrokes() }
        return copy(elements = unnumberedElements)
    }

    fun withBoldedStroke(strokeNum: Int, width: Int = 7): KanjiVG {
        val strokeElements = elements.map { it.withStrokeInBold(strokeNum, width) }
        return copy(elements = strokeElements)
    }

    companion object {
        const val SVG_NAMESPACE = "http://www.w3.org/2000/svg"
        const val SVG_PREFIX = "" // on purpose

        const val KANJIVG_NAMESPACE = "http://kanjivg.tagaini.net"
        const val KANJIVG_PREFIX = "kvg"

        const val SVG_PRESERVE_ASPECT_RATIO_DEFAULT = "xMidYMid meet"
        const val SVG_VERSION_DEFAULT = "1.0"
        const val SVG_CONTENT_SCRIPT_TYPE_DEFAULT = "text/ecmascript"
        const val SVG_CONTENT_STYLE_TYPE_DEFAULT = "text/css"

        val XML_PARSER = XML { autoPolymorphic = true }

        fun loadSvg(symbol: Char, basePath: File): KanjiVG {
            val kanjiCode = symbol.code.toString(16).padStart(5, '0')

            val kanjiFile = File(basePath, "$kanjiCode.svg")
            val kanjiSvgRaw = kanjiFile.readText()

            return XML_PARSER.decodeFromString<KanjiVG>(kanjiSvgRaw)
        }
    }
}