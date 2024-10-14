package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.support

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint
import net.gregorbg.lang.japanese.kanji.util.cycle

data class Rectangle(
    val startCorner: GeomPoint,
    val endCorner: GeomPoint,
) {
    val topCorner: GeomPoint
        get() = GeomPoint(endCorner.x, startCorner.y)

    val bottomCorner: GeomPoint
        get() = GeomPoint(startCorner.x, endCorner.y)

    val center: GeomPoint
        get() = this.startCorner + this.diagonal() / 2

    val width: Float
        get() = this.endCorner.x - this.startCorner.x

    val height: Float
        get() = this.endCorner.y - this.startCorner.y

    operator fun contains(point: GeomPoint): Boolean {
        return point.x >= startCorner.x && point.x <= endCorner.x && point.y >= startCorner.y && point.y <= endCorner.y
    }

    fun cornersCw(): List<GeomPoint> {
        return listOf(
            startCorner,
            topCorner,
            endCorner,
            bottomCorner,
        )
    }

    fun cornersCcw(): List<GeomPoint> {
        return this.cornersCw().reversed()
    }

    fun cornersOrdered(cw: Boolean = true): List<GeomPoint> {
        return if (cw) this.cornersCw() else this.cornersCcw()
    }

    fun cwSideIndex(borderPoint: GeomPoint): Int {
        if (borderPoint.y == startCorner.y) {
            return 0
        }

        if (borderPoint.x == endCorner.x) {
            return 1
        }

        if (borderPoint.y == endCorner.y) {
            return 2
        }

        if (borderPoint.x == startCorner.x) {
            return 3
        }

        return -1
    }

    fun getCorner(indexCw: Int): GeomPoint {
        return this.cornersCw()[indexCw]
    }

    fun getCornersPath(start: GeomPoint, end: GeomPoint, clockwise: Boolean, stackSafeguard: Boolean = false): List<GeomPoint> {
        val startIdx = cwSideIndex(start)
        val endIdx = cwSideIndex(end)

        if (startIdx >= endIdx && !stackSafeguard)
            return getCornersPath(end, start, clockwise, true)

        val rawIndices = if (clockwise) {
            (startIdx + 1 until endIdx + 1)
        } else {
            (endIdx + 1 until startIdx + 1 + 4)
        }

        return rawIndices.map { it % 4 }.map(this::getCorner)
    }

    private fun axisScaling(pos: Float, normalComp: Float, maxBound: Float): Float {
        return if (normalComp != 0f) {
            if (normalComp > 0) (maxBound - pos) / normalComp else -pos / normalComp
        } else {
            Float.MAX_VALUE
        }
    }

    fun scalingFactor(inscribed: GeomPoint, direction: GeomPoint): Float {
        val xScaling = axisScaling(inscribed.x, direction.x, this.width)
        val yScaling = axisScaling(inscribed.y, direction.y, this.height)

        return minOf(xScaling, yScaling)
    }

    fun diagonal(): GeomPoint {
        return this.startCorner.segmentTo(this.endCorner)
    }

    fun centerCornerRays(): List<GeomPoint> {
        return this.cornersCw().map { this.center.segmentTo(it) }
    }

    fun translate(translation: GeomPoint): Rectangle {
        return Rectangle(
            this.startCorner.translate(translation),
            this.endCorner.translate(translation),
        )
    }
}
