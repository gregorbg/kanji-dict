package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.abs
import kotlin.math.pow

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times
import kotlin.math.PI
import kotlin.math.atan2

data class BezierCurve(val controlPoints: List<GeomPoint>) : PathComponent<BezierCurve> {
    constructor(vararg controlPoints: GeomPoint) : this(controlPoints.asList())

    override val start: GeomPoint
        get() = this.controlPoints.first()
    override val end: GeomPoint
        get() = this.controlPoints.last()

    override val orderedPoints = this.controlPoints

    val degree: Int
        get() = this.controlPoints.size - 1

    override fun positionAt(t: Float) = this.bernstein(t)

    protected tailrec fun deCasteljau(t: Float, iter: List<GeomPoint>): GeomPoint {
        val larpLines = iter.zipWithNext()

        if (larpLines.isEmpty())
            return iter.first()

        val nextIter = larpLines.map { (a, b) -> Line(a, b) }
            .map { it.positionAt(t) }

        return deCasteljau(t, nextIter)
    }

    protected fun bernstein(t: Float): GeomPoint {
        return (0..this.degree)
            .map { this.bernsteinPolynomial(it, t) * this.controlPoints[it] }
            .reduce(GeomPoint::plus)
    }

    private fun bernsteinPolynomial(i: Int, t: Float): Float {
        return binomCoeff(this.degree, i) * t.pow(i) * (1 - t).pow(this.degree - i)
    }

    fun derivative(): BezierCurve {
        return BezierCurve(this.derivativeControls())
    }

    override fun reverse(): BezierCurve {
        return BezierCurve(this.controlPoints.reversed())
    }

    override fun velocityAt(t: Float): GeomPoint = this.derivative().velocityAt(t)

    protected fun derivativeControls(): List<GeomPoint> {
        return (0 until this.degree).map { this.degree * (this.controlPoints[it + 1] - this.controlPoints[it]) }
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

    override fun extendContinuous(): BezierCurve {
        return BezierCurve(this.computeContinuation())
    }

    private fun computeContinuation(accu: List<GeomPoint> = emptyList(), derivateBase: BezierCurve = this): List<GeomPoint> {
        if (accu.size == this.degree + 1) {
            return accu
        }

        val numerator = derivateBase.positionAt(1f)
        val denominator = this.degree.factorial() / (this.degree - accu.size).factorial()

        val patchSum = accu.reversed()
            .mapIndexed { idx, pt ->
                val k = idx + 1
                val i = accu.size

                (-1f).pow(k) * binomCoeff(i, k) * pt
            }.fold(GeomPoint(0f, 0f), GeomPoint::plus)

        val nextPoint = (numerator / denominator) - patchSum

        return computeContinuation(accu + nextPoint, derivateBase.derivative())
    }

    fun controlBoundingBox(): Rectangle {
        return computeBoundingBox(this.controlPoints)
    }

    fun tightBoundingBox(): Rectangle {
        //val localExtremes = this.derivative().zeros()
        val localExtremes = listOf<Float>()

        val candidateMeasures = listOf(0f, 1f) + localExtremes
        val candidatePoints = candidateMeasures.map { this.positionAt(it) }

        return computeBoundingBox(candidatePoints)
    }

    fun optimalBoundingBox(boxEstimateFn: BezierCurve.() -> Rectangle = BezierCurve::controlBoundingBox): Pair<Rectangle, Float> {
        val originTranslation = -this.start
        val translatedToOrigin = this.translate(originTranslation)

        val alignmentRotation = -translatedToOrigin.end.angleToXAxis()
        val normalized =  translatedToOrigin.rotate(alignmentRotation)

        val rawBoundingBox = normalized.boxEstimateFn()

        return rawBoundingBox.translate(-originTranslation) to -alignmentRotation
    }

    fun translate(translation: GeomPoint): BezierCurve {
        val translatedPoints = this.controlPoints.map { it.translate(translation) }

        return BezierCurve(translatedPoints)
    }

    fun rotate(angleRadians: Float): BezierCurve {
        val rotatedPoints = this.controlPoints.map { it.rotate(angleRadians) }

        return BezierCurve(rotatedPoints)
    }

    companion object {
        const val ARC_LENGTH_EPSILON = 0.00001f

        private fun Int.factorial(): Long {
            return (1..this).fold(1, Long::times)
        }

        private fun Int.boundedFactorial(factors: Int): Long {
            return ((this - factors)..this).fold(1, Long::times)
        }

        private fun binomCoeff(n: Int, k: Int): Long {
            val denominator = k.factorial() * (n - k).factorial()
            return n.factorial() / denominator
        }

        private fun computeBoundingBox(points: List<GeomPoint>): Rectangle {
            val start = GeomPoint(
                points.minOf { it.x },
                points.minOf { it.y }
            )

            val end = GeomPoint(
                points.maxOf { it.x },
                points.maxOf { it.y }
            )

            return Rectangle(start, end)
        }
    }
}