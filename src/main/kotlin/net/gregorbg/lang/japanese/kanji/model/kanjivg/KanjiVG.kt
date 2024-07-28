package net.gregorbg.lang.japanese.kanji.model.kanjivg

import net.gregorbg.lang.japanese.kanji.model.kanjivg.enumeration.ZoomAndPan
import net.gregorbg.lang.japanese.kanji.model.kanjivg.enumeration.ZoomAndPan.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.PathComponent
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.Rectangle
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.svgPath
import net.gregorbg.lang.japanese.kanji.util.cycle
import net.gregorbg.lang.japanese.kanji.util.math.PerlinRandom
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.io.File
import kotlin.math.absoluteValue

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

    fun withCoffeeStains(
        coverPath: PathComponent<*>,
        noisePoints: Int,
        globalScaling: Float,
        includeClockwise: Boolean = true,
    ): KanjiVG {
        val boundingRect = this.boundingRectangle()

        val perlin = PerlinRandom(this.hashCode())
        val perlinOffsets = perlin.generateNoise(noisePoints)

        val controlPoints = perlinOffsets.mapIndexed { i, p ->
            val positionIndex = (i + 1f) / (noisePoints + 1)

            val linePosition = coverPath.positionAt(positionIndex)
            val linePerpendicular = coverPath.velocityAt(positionIndex).perpendicular()

            val minScaling = linePosition.minScaling(linePerpendicular, boundingRect)
            val maxScaling = linePosition.maxScaling(linePerpendicular, boundingRect)

            val lineMaxPerpendicular = if (p >= 0) linePerpendicular * maxScaling else linePerpendicular * minScaling

            linePosition + p.absoluteValue * lineMaxPerpendicular * globalScaling
        }

        val (firstCp, secondCp) = controlPoints.take(2)
        val remainingControlPoints = controlPoints.drop(2)

        val path = svgPath(coverPath.start.x, coverPath.start.y) {
            val initialEnd = remainingControlPoints.firstOrNull() ?: coverPath.end

            C(firstCp.x, firstCp.y, secondCp.x, secondCp.y, initialEnd.x, initialEnd.y)

            val followUp = remainingControlPoints.drop(1).chunked(2)

            for (cps in followUp) {
                val symmStartPoint = cps.first()
                val symmEndPoint = cps.getOrNull(1) ?: coverPath.end

                S(symmStartPoint.x, symmStartPoint.y, symmEndPoint.x, symmEndPoint.y)
            }

            val coverPathEndVelocity = coverPath.velocityAt(1f)

            val maxScalingEnd = coverPath.end.maxScaling(coverPathEndVelocity, boundingRect)
            val maxScaledEnd = coverPath.end + maxScalingEnd * coverPathEndVelocity

            val cornerSideIndexEnd = boundingRect.cwSideIndex(maxScaledEnd)

            val coverPathStartVelocity = coverPath.velocityAt(0f)

            val minScalingEnd = coverPath.start.minScaling(coverPathStartVelocity, boundingRect)
            val minScaledEnd = coverPath.start + minScalingEnd * coverPathStartVelocity

            val cornerSideIndexStartBase = boundingRect.cwSideIndex(minScaledEnd)
            val cornerSideIndexStart = if (cornerSideIndexStartBase <= cornerSideIndexEnd) cornerSideIndexStartBase + 4 else cornerSideIndexStartBase

            val containedCorners = boundingRect.cornersCw().cycle(cornerSideIndexEnd + 1).take(cornerSideIndexStart - cornerSideIndexEnd)
            val includedCorners = if (includeClockwise) containedCorners else boundingRect.cornersCw().minus(containedCorners).reversed()

            for (ics in includedCorners) {
                L(ics.x, ics.y)
            }

            Z()
        }

        val pathElement = SvgElement.Path("coffeeStainPath", path.toSvg())
        val pathGroup = SvgElement.Group("coffeeStain", style = "fill:darkgoldenrod;stroke:black;", elements = listOf(pathElement))

        return this.copy(elements = this.elements + pathGroup)
    }

    fun boundingRectangle(): Rectangle {
        return Rectangle(
            GeomPoint(0f, 0f),
            GeomPoint(this.width.toFloat(), this.height.toFloat()),
        )
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