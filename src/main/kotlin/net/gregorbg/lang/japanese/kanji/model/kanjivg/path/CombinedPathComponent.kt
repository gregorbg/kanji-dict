package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.abs

data class CombinedPathComponent(
    val segments: List<PathComponent<*>>,
) : PathComponent<CombinedPathComponent>, List<PathComponent<*>> by segments {
    override val start: GeomPoint
        get() = this.segments.firstOrNull()?.start ?: error("Cannot determine the start of an empty path")
    override val end: GeomPoint
        get() = this.segments.lastOrNull()?.end ?: error("Cannot determine the end of an empty path")

    override val orderedPoints: List<GeomPoint>
        get() = this.segments.flatMap { it.orderedPoints }.distinct()

    override fun arcLength(): Float {
        return this.segments.map { it.arcLength() }.sum()
    }

    private fun <T> componentFnAt(t: Float, componentFn: PathComponent<*>.(Float) -> T): T {
        val arcLengths = this.segments.map { it.arcLength() }
        val totalArcLength = arcLengths.sum()

        val relArcLengths = arcLengths.map { it / totalArcLength }
        val runningArcSums = relArcLengths.runningReduce { a, b -> a + b }

        val lookupIndex = runningArcSums.indexOfFirst { it + FLOAT_ROUNDING_ERR >= t }
        val lookupSegment = this.segments[lookupIndex]

        val untilT = relArcLengths.take(lookupIndex).sum()
        val startToT = abs(t - untilT)

        val rescaledT = startToT / relArcLengths[lookupIndex]

        return lookupSegment.componentFn(rescaledT)
    }

    override fun positionAt(t: Float): GeomPoint {
        return this.componentFnAt(t) { positionAt(it) }
    }

    override fun velocityAt(t: Float): GeomPoint {
        return this.componentFnAt(t) { velocityAt(it) }
    }

    override fun reverse(): CombinedPathComponent {
        return CombinedPathComponent(
            this.segments.map { it.reverse() }.reversed()
        )
    }

    override fun extendContinuous(): CombinedPathComponent {
        val extendedLast = this.segments.lastOrNull()?.extendContinuous() ?: error("Cannot extend an empty path")
        return CombinedPathComponent(listOf(extendedLast))
    }

    override fun extendLine(): Line {
        return this.segments.lastOrNull()?.extendLine() ?: error("Cannot extend an empty path")
    }

    override fun translate(translation: GeomPoint): CombinedPathComponent {
        val translatedParts = this.segments.map { it.translate(translation) }
        return CombinedPathComponent(translatedParts)
    }

    companion object {
        const val FLOAT_ROUNDING_ERR = .0001f
    }
}
