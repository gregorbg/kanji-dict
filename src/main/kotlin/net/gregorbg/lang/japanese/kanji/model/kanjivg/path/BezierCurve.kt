package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.abs
import kotlin.math.pow

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.support.Circle
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.support.Rectangle
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class BezierCurve(val controlPoints: List<GeomPoint>) {
    constructor(vararg controlPoints: GeomPoint) : this(controlPoints.asList())

    val start: GeomPoint
        get() = this.controlPoints.first()
    val end: GeomPoint
        get() = this.controlPoints.last()

    val orderedPoints = this.controlPoints

    val degree: Int
        get() = this.controlPoints.size - 1

    fun positionAt(t: Float) = this.bernstein(t)

    protected tailrec fun deCasteljau(t: Float, iter: List<GeomPoint>): GeomPoint {
        val larpLines = iter.zipWithNext()

        if (larpLines.isEmpty())
            return iter.first()

        val nextIter = larpLines.map { (a, b) -> BezierCurve(a, b) }
            .map { it.positionAt(t) }

        return deCasteljau(t, nextIter)
    }

    protected fun bernstein(t: Float): GeomPoint {
        return (0..this.degree)
            .map { this.bernsteinPolynomial(it, t) * this.controlPoints[it] }
            .fold(GeomPoint.origin, GeomPoint::plus)
    }

    private fun bernsteinPolynomial(i: Int, t: Float): Float {
        return binomCoeff(this.degree, i) * t.pow(i) * (1 - t).pow(this.degree - i)
    }

    protected fun algebraicPos(t: Float): GeomPoint {
        return (0..this.degree)
            .map { t.pow(it) * this.algebraicPolynomial(it) }
            .fold(GeomPoint.origin, GeomPoint::plus)
    }

    fun polynomialCoefficients(): List<GeomPoint> {
        return (0..this.degree).reversed().map { algebraicPolynomial(it) }
    }

    private fun algebraicPolynomial(j: Int): GeomPoint {
        val sum = (0..j).map {
            val sign = (-1f).pow(it + j)
            val denominator = it.factorial() * (j - it).factorial()

            (this.controlPoints[it] * sign) / denominator
        }.fold(GeomPoint.origin, GeomPoint::plus)

        val binomScaling = this.degree.factorial() / (this.degree - j).factorial()

        return binomScaling * sum
    }

    fun derivative(): BezierCurve {
        return BezierCurve(this.derivativeControls())
    }

    fun reverse(): BezierCurve {
        return BezierCurve(this.controlPoints.reversed())
    }

    fun arcSectionToParam(p: Float): Float {
        val lookupPoints = this.convergingLookupPoints(ARC_LENGTH_EPSILON)

        val stableLut = lookupPoints.zipWithNext()
            .map { (a, b) -> BezierCurve(a, b) }

        val (lookupIndex, baseUnitT) = arcLengthRescaling(p, stableLut)
        return (lookupIndex.toFloat() / stableLut.size) + baseUnitT * (1f / stableLut.size)
    }

    protected fun derivativeControls(): List<GeomPoint> {
        return (0 until this.degree).map { this.degree * (this.controlPoints[it + 1] - this.controlPoints[it]) }
    }

    fun arcLength() = this.arcLength(ARC_LENGTH_EPSILON)

    fun arcLength(epsilon: Float): Float {
        val lut = convergingLookupPoints(epsilon)
        return lerpArcLength(lut)
    }

    private tailrec fun convergingLookupPoints(
        epsilon: Float,
        accu: List<GeomPoint> = listOf(this.start, this.end)
    ): List<GeomPoint> {
        val stepSize = 1f / accu.size

        val nextAccu = (0..accu.size)
            .map { it * stepSize }
            .map { this.positionAt(it) }

        val segmentSize = lerpArcLength(nextAccu)
        val prevLength = lerpArcLength(accu)

        val error = abs(segmentSize - prevLength)

        if (error < epsilon) {
            return nextAccu
        }

        return convergingLookupPoints(epsilon, nextAccu)
    }

    fun curveRoots(): List<Float> {
        if (this.degree == 1) {
            val (m, n) = this.polynomialCoefficients()

            return listOf(
                solveLinear(m.x, n.x),
                solveLinear(m.y, n.y),
            ).flatten().filter { it >= 0 && it <= 1 }.distinct()
        } else if (this.degree == 2) {
            val (a, b, c) = this.polynomialCoefficients()

            return listOf(
                solveQuadratic(a.x, b.x, c.x),
                solveQuadratic(a.y, b.y, c.y),
            ).flatten().filter { it >= 0 && it <= 1 }.distinct()
        } else
            return error("Cannot solve equations >= rank 3")
    }

    fun extendLine(): BezierCurve {
        val mirrorExtension = this.derivative().positionAt(1f) / this.degree

        return BezierCurve(
            this.end,
            this.end + mirrorExtension
        )
    }

    fun extendContinuous(): BezierCurve {
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
            }.fold(GeomPoint.origin, GeomPoint::plus)

        val nextPoint = (numerator / denominator) - patchSum

        return computeContinuation(accu + nextPoint, derivateBase.derivative())
    }

    fun controlBoundingBox(): Rectangle {
        return computeBoundingBox(this.controlPoints)
    }

    private fun extremePoints(): List<GeomPoint> {
        val localExtremes = this.derivative().curveRoots()
        val candidateMeasures = listOf(0f, 1f) + localExtremes

        return candidateMeasures.distinct()
            .map { this.positionAt(it) }
    }

    fun tightBoundingBox(): Rectangle {
        val candidatePoints = this.extremePoints()
        return computeBoundingBox(candidatePoints)
    }

    fun rotateBoundingBox(originalBoxFn: BezierCurve.() -> Rectangle): List<GeomPoint> {
        val originTranslation = -this.start
        val translatedToOrigin = this.translate(originTranslation)

        val alignmentRotation = -translatedToOrigin.end.angleToXAxis()
        val normalized =  translatedToOrigin.rotate(alignmentRotation)

        val rawBoundingBox = normalized.originalBoxFn()

        return rawBoundingBox.cornersCw()
            .map { it.rotate(-alignmentRotation) }
            .map { it.translate(-originTranslation) }
    }

    fun coveringCircle(additionalPoints: List<GeomPoint> = emptyList()): Circle {
        val candidatePoints = this.extremePoints() + this.controlPoints + additionalPoints
        val convexHull = GeomPoint.grahamScan(candidatePoints)

        return Circle.Companion.findMinimalEnclosingCircle(convexHull)
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

        // see https://gist.github.com/mikhailov-work/0d177465a8151eb6ede1768d51d476c7
        val TURBO_COEFFICIENTS = listOf(
            listOf(0.13572138, 4.61539260, -42.66032258, 132.13108234, -152.94239396, 59.28637943),
            listOf(0.09140261, 2.19418839, 4.84296658, -14.18503333, 4.27729857, 2.82956604),
            listOf(0.10667330, 12.64194608, -60.58204836, 110.36276771, -89.90310912, 27.34824973),
        )

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

        private fun solveLinear(m: Float, n: Float): List<Float> {
            if (m == 0f) return emptyList()

            return listOf(-n / m)
        }

        private fun solveQuadratic(a: Float, b: Float, c: Float): List<Float> {
            val rootTerm = b.pow(2) - 4 * a * c

            if (rootTerm < 0)
                return emptyList()

            val pos = (-b + sqrt(rootTerm)) / (2 * a)
            val neg = (-b - sqrt(rootTerm)) / (2 * a)

            return listOf(pos, neg).distinct()
        }

        fun fromPolynomial(polyCoefficients: List<GeomPoint>): BezierCurve {
            val polyControlPoints = computePolynomial(polyCoefficients)
            return BezierCurve(polyControlPoints)
        }

        private tailrec fun computePolynomial(polyCoefficients: List<GeomPoint>, accu: List<GeomPoint> = emptyList()): List<GeomPoint> {
            if (accu.size == polyCoefficients.size)
                return accu

            val nextControl = polyCoefficients
                .reversed()
                .subList(0, accu.size + 1)
                .mapIndexed { idx, coeff -> binomCoeff(accu.size, idx) * coeff / binomCoeff(polyCoefficients.size - 1, idx) }
                .fold(GeomPoint.origin, GeomPoint::plus)

            return computePolynomial(polyCoefficients, accu + nextControl)
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

        private fun rgbColormap(mapping: (Int) -> Double): Triple<Int, Int, Int> {
            val (r, g, b) = (0 until 3)
                .map(mapping)
                .map { (255 * it).roundToInt() }

            return Triple(r, g, b)
        }

        fun rgbSineColormap(t: Float): Triple<Int, Int, Int> {
            return rgbColormap { (sin(2 * PI * (t + it / 3f)) + 1) / 2 }
        }

        fun rgbTurboColormap(t: Float): Triple<Int, Int, Int> {
            return rgbColormap {
                TURBO_COEFFICIENTS[it].reduceIndexed { i, sum, coeff -> sum + coeff * t.pow(i) }
            }
        }

        private fun lerpArcLength(lutPoints: List<GeomPoint>): Float {
            return lutPoints
                .zipWithNext()
                .map { (a, b) -> a.distanceTo(b) }
                .sum()
        }

        fun arcLengthRescaling(p: Float, components: List<BezierCurve>): Pair<Int, Float> {
            val cumulativeArcLengths = components.runningFold(0f) { arcAccu, component -> arcAccu + component.arcLength() }
            val totalArcLength = cumulativeArcLengths.last()

            val lookupIndex = cumulativeArcLengths.indexOfLast { it / totalArcLength <= p }

            val curveStartLength = cumulativeArcLengths[lookupIndex]
            val curveEndLength = cumulativeArcLengths[lookupIndex + 1]

            val localT = (p * totalArcLength - curveStartLength) / (curveEndLength - curveStartLength)

            return lookupIndex to localT
        }
    }
}
