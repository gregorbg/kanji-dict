package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.pow
import kotlin.math.round
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

    fun toSvg(): String = "${toSvgNumber(this.x)},${toSvgNumber(this.y)}"

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

        fun toSvgNumber(value: Float): String {
            val rounded = round(value * 100) / 100
            return rounded.toString().dropLastWhile { it == '0' }.trimEnd('.')
        }
    }
}
