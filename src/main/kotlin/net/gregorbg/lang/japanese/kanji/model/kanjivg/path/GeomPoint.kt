package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.pow
import kotlin.math.sqrt

data class GeomPoint(
    val x: Float,
    val y: Float,
) {
    operator fun plus(other: GeomPoint): GeomPoint {
        return GeomPoint(
            this.x + other.x,
            this.y + other.y,
        )
    }

    operator fun minus(other: GeomPoint): GeomPoint {
        return GeomPoint(
            this.x - other.x,
            this.y - other.y,
        )
    }

    fun distanceTo(other: GeomPoint): Float {
        val distanceVec = other - this

        val sumX = distanceVec.x.pow(2)
        val sumY = distanceVec.y.pow(2)

        return sqrt(sumX + sumY)
    }

    fun toSvg(): String = "${this.x},${this.y}"

    companion object {
        operator fun Float.times(point: GeomPoint): GeomPoint {
            return GeomPoint(
                this * point.x,
                this * point.y,
            )
        }

        operator fun Int.times(point: GeomPoint): GeomPoint {
            return GeomPoint(
                this * point.x,
                this * point.y,
            )
        }
    }
}
