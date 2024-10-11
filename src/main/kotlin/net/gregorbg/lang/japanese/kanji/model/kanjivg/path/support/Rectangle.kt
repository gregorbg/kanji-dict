package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.support

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint

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
