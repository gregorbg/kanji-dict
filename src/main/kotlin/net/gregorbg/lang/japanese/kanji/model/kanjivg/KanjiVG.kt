package net.gregorbg.lang.japanese.kanji.model.kanjivg

import net.gregorbg.lang.japanese.kanji.model.kanjivg.enumeration.ZoomAndPan
import net.gregorbg.lang.japanese.kanji.model.kanjivg.enumeration.ZoomAndPan.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import net.gregorbg.lang.japanese.kanji.model.kanjivg.grammar.SvgPathReader
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.Command
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.ParsedPath
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.svgPath
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.support.Rectangle
import net.gregorbg.lang.japanese.kanji.util.XmlUtils
import net.gregorbg.lang.japanese.kanji.util.math.PerlinRandom
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.sign

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
        coverPath: BezierCurve,
        noisePoints: Int,
        globalScaling: Float = 1f,
        includeClockwise: Boolean = true,
    ): KanjiVG {
        val boundingRect = this.boundingRectangle()

        val perlin = PerlinRandom(this.hashCode())
        val perlinOffsets = perlin.generateNoise(noisePoints)

        val coverPathVelocity = coverPath.derivative()

        val controlPoints = perlinOffsets.mapIndexed { i, noise ->
            val t = (i + 1f) / (noisePoints + 2f)

            val curveParam = coverPath.arcSectionToParam(t)

            val position = coverPath.positionAt(curveParam)
            val velocity = coverPathVelocity.positionAt(curveParam)

            val unitNormal = velocity.perpendicular().unit()

            val randomizedNormal = unitNormal * noise.sign

            val potentialScaling = boundingRect.scalingFactor(position, randomizedNormal)
            val actualScaling = min(noise.absoluteValue, potentialScaling)

            position + randomizedNormal * actualScaling * globalScaling
        }

        val path = svgPath(coverPath.start) {
            val (c1, c2, e) = controlPoints.take(3)
            C(c1.x, c1.y, c2.x, c2.y, e.x, e.y)

            val remainingStainPoints = controlPoints.drop(3) + coverPath.end
            remainingStainPoints.chunked(2).forEach { (c2, e) ->
                S(c2.x, c2.y, e.x, e.y)
            }

            val coveringCorners = boundingRect.getCornersPath(coverPath.start, coverPath.end, includeClockwise)
            coveringCorners.forEach {
                L(it.x, it.y)
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

    fun strokePaths(): List<ParsedPath> {
        val strokePathsGroup = this.elements
            .filterIsInstance<SvgElement.Group>()
            .find { "StrokePaths" in it.id }

        val strokePaths = strokePathsGroup
            ?.recursiveElements()
            .orEmpty()
            .filterIsInstance<SvgElement.Path>()

        return strokePaths.map { SvgPathReader.parse(it.d) }
    }

    fun withConnectedStrokes(): KanjiVG {
        val paths = this.strokePaths()

        val joiningPaths = paths.asSequence()
            .map { it.trace() }
            .windowed(2)
            .map { (prevSpline, nextSpline) ->
                val prevExtension = prevSpline.last().extendLine()
                val nextExtension = nextSpline.first().reverse().extendLine()

                BezierCurve(prevExtension.start, prevExtension.end, nextExtension.end, nextExtension.start)
            }
            .map {
                svgPath(it.start) {
                    C(it.controlPoints[1].x, it.controlPoints[1].y, it.controlPoints[2].x, it.controlPoints[2].y, it.end.x, it.end.y)
                }
            }
            .map { SvgElement.Path("connect${it.hashCode()}", it.toSvg()) }
            .toList()

        val joiningPathGroup = SvgElement.Group("joining-path-group", elements = joiningPaths, style = "fill:none;stroke:black;stroke-width:3;stroke-linecap:round;stroke-linejoin:round;")
        return this.copy(elements = this.elements + joiningPathGroup)
    }

    fun markedStrokes(): KanjiVG {
        val visibleCommands = this.strokePaths()
            .flatMap { it.commands }
            .filter { it.command != Command.MOVE_TO && it.command != Command.CLOSE_PATH }

        val newPaths = visibleCommands
            .mapIndexed { idx, cmd ->
                val newPath = svgPath(cmd.tracingCurve.start) { imitate(cmd) }

                val colorIndex = idx.toFloat() / (visibleCommands.size - 1)
                val (r, g, b) = BezierCurve.rgbTurboColormap(colorIndex)

                SvgElement.Path("colorMark${cmd.hashCode()}", newPath.toSvg(), stroke = "rgb($r, $g, $b)")
            }

        val joiningPathGroup = SvgElement.Group("coloring-path", elements = newPaths, style = "fill:none;")
        return this.copy(elements = this.elements + joiningPathGroup)
    }

    companion object : StringFormat by XmlUtils.XML_PARSER {
        const val SVG_NAMESPACE = "http://www.w3.org/2000/svg"
        const val SVG_PREFIX = "" // on purpose

        const val KANJIVG_NAMESPACE = "http://kanjivg.tagaini.net"
        const val KANJIVG_PREFIX = "kvg"

        const val SVG_PRESERVE_ASPECT_RATIO_DEFAULT = "xMidYMid meet"
        const val SVG_VERSION_DEFAULT = "1.0"
        const val SVG_CONTENT_SCRIPT_TYPE_DEFAULT = "text/ecmascript"
        const val SVG_CONTENT_STYLE_TYPE_DEFAULT = "text/css"

        fun loadSvg(symbol: Char, basePath: File): KanjiVG {
            val kanjiCode = symbol.code.toString(16).padStart(5, '0')

            val kanjiFile = File(basePath, "$kanjiCode.svg")
            val kanjiSvgRaw = kanjiFile.readText()

            return this.decodeFromString<KanjiVG>(kanjiSvgRaw)
        }
    }
}