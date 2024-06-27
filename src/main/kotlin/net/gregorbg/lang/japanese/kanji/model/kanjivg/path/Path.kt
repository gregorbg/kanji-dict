package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.abs

data class Path(
    override val start: GeomPoint,
    val relations: List<PathStep>,
    val segments: List<PathPrimitive<*>>,
) : PathPrimitive<Path> {
    override val end: GeomPoint
        get() = this.segments.lastOrNull()?.end ?: start

    override fun toSvg(): String {
        return """
            M ${this.start.toSvg()}
            ${this.segments.joinToString { it.toSvg() }}
        """.trimIndent()
    }

    override fun arcLength(): Float {
        return this.segments.map { it.arcLength() }.sum()
    }

    override fun positionAt(t: Float): GeomPoint {
        val arcLengths = this.segments.map { it.arcLength() }
        val totalArcLength = arcLengths.sum()

        val relArcLengths = arcLengths.map { it / totalArcLength }
        val runningArcSums = relArcLengths.runningReduce { a, b -> a + b }

        val lookupIndex = runningArcSums.indexOfFirst { it >= t }
        val lookupSegment = this.segments[lookupIndex]

        val untilT = relArcLengths.take(lookupIndex).sum()
        val startToT = abs(t - untilT)

        val rescaledT = startToT / relArcLengths[lookupIndex]

        return lookupSegment.positionAt(rescaledT)
    }

    override fun reverse(): Path {
        return Path(
            this.end,
            this.relations.reversed(),
            this.segments.map { it.reverse() }.reversed()
        )
    }
}
