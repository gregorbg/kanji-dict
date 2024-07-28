package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.abs

abstract class BezierCurve<T : BezierCurve<T>>(vararg val controlPoints: GeomPoint) : PathComponent<T> {
    override val orderedPoints: List<GeomPoint>
        get() = listOf(this.start) + this.controlPoints + listOf(this.end)

    override fun positionAt(t: Float): GeomPoint {
        val initialSegments = listOf(this.start) + this.controlPoints.asList() + listOf(this.end)
        return deCasteljau(t, initialSegments)
    }

    private tailrec fun deCasteljau(t: Float, iter: List<GeomPoint>): GeomPoint {
        val larpLines = iter.zipWithNext()

        if (larpLines.isEmpty())
            return iter.first()

        val nextIter = larpLines.map { (a, b) -> Line(a, b) }
            .map { it.positionAt(t) }

        return deCasteljau(t, nextIter)
    }

    override fun arcLength() = this.arcLength(ARC_LENGTH_EPSILON)

    fun arcLength(epsilon: Float): Float {
        val baseLength = this.start.distanceTo(this.end)

        return arcLengthRec(2, baseLength, epsilon)
    }

    private tailrec fun arcLengthRec(numSegments: Int, prevLength: Float, epsilon: Float): Float {
        val stepSize = 1f / numSegments

        val segmentSize = (0..numSegments).map { it * stepSize }
            .map { this.positionAt(it) }
            .zipWithNext()
            .map { (a, b) -> a.distanceTo(b) }
            .sum()

        val error = abs(segmentSize - prevLength)

        if (error < epsilon) {
            return segmentSize
        }

        return arcLengthRec(numSegments + 1, segmentSize, epsilon)
    }

    override fun extendLine(): Line {
        return Line(
            this.end,
            this.end + this.velocityAt(1f)
        )
    }

    companion object {
        const val ARC_LENGTH_EPSILON = 0.00001f
    }
}