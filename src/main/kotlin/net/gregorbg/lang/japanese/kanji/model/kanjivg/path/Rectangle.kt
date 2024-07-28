package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

data class Rectangle(
    val startCorner: GeomPoint,
    val endCorner: GeomPoint,
) {
    operator fun contains(point: GeomPoint): Boolean {
        return point.x >= startCorner.x && point.x <= endCorner.x && point.y >= startCorner.y && point.y <= endCorner.y
    }

    fun cornersCw(): List<GeomPoint> {
        return listOf(
            startCorner,
            GeomPoint(endCorner.x, startCorner.y),
            endCorner,
            GeomPoint(startCorner.x, endCorner.y),
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
}
