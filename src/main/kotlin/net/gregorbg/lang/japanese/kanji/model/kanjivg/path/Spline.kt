package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

data class Spline(
    val segments: List<PathComponent<*>>,
) : PathComponent<Spline>, List<PathComponent<*>> by segments {
    constructor(vararg controlPoints: PathComponent<*>) : this(controlPoints.asList())

    override val start: GeomPoint
        get() = this.firstOrNull()?.start ?: error("Cannot determine the start of an empty path")
    override val end: GeomPoint
        get() = this.lastOrNull()?.end ?: error("Cannot determine the end of an empty path")

    override val orderedPoints: List<GeomPoint>
        get() = this.flatMap { it.orderedPoints }.distinct()

    override fun arcLength(): Float {
        return this.map { it.arcLength() }.sum()
    }

    private fun <T> componentFnAt(t: Float, componentFn: PathComponent<*>.(Float) -> T): T {
        val lookupIndex = (t * this.size).toInt().coerceAtMost(this.size - 1)
        val lookupSegment = this[lookupIndex]

        val rescaledT = (t - lookupIndex / this.size) * this.size

        return lookupSegment.componentFn(rescaledT)
    }

    override fun positionAt(t: Float): GeomPoint {
        return this.componentFnAt(t) { positionAt(it) }
    }

    override fun velocityAt(t: Float): GeomPoint {
        return this.componentFnAt(t) { velocityAt(it) }
    }

    override fun positionForArc(t: Float): GeomPoint {
        val (lookupIndex, rescaledT) = BezierCurve.arcLengthRescaling(t, this)
        val lookupSegment = this[lookupIndex]

        return lookupSegment.positionForArc(rescaledT)
    }

    override fun reverse(): Spline {
        val reversedParts = this.map { it.reverse() }.reversed()
        return Spline(reversedParts)
    }

    override fun extendContinuous(): Spline {
        val extendedLast = this.lastOrNull()?.extendContinuous() ?: error("Cannot extend an empty path")
        return Spline(listOf(extendedLast))
    }

    override fun extendLine(): Line {
        return this.lastOrNull()?.extendLine() ?: error("Cannot extend an empty path")
    }

    override fun translate(translation: GeomPoint): Spline {
        val translatedParts = this.map { it.translate(translation) }
        return Spline(translatedParts)
    }

    companion object {
        const val FLOAT_ROUNDING_ERR = .0001f
    }
}
