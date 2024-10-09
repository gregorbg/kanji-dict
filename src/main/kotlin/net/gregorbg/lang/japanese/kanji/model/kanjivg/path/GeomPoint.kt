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

    operator fun times(other: Float): GeomPoint {
        return GeomPoint(
            this.x * other,
            this.y * other,
        )
    }

    operator fun times(other: Int): GeomPoint {
        return GeomPoint(
            this.x * other,
            this.y * other,
        )
    }

    operator fun times(other: Long): GeomPoint {
        return GeomPoint(
            this.x * other,
            this.y * other,
        )
    }

    operator fun div(other: Float): GeomPoint {
        return GeomPoint(
            this.x / other,
            this.y / other,
        )
    }

    operator fun div(other: Int): GeomPoint {
        return GeomPoint(
            this.x / other,
            this.y / other,
        )
    }

    operator fun div(other: Long): GeomPoint {
        return GeomPoint(
            this.x / other,
            this.y / other,
        )
    }

    operator fun unaryPlus(): GeomPoint {
        return this.copy()
    }

    operator fun unaryMinus(): GeomPoint {
        return -1 * this
    }

    fun segmentTo(other: GeomPoint): GeomPoint {
        return other - this
    }

    fun distanceTo(other: GeomPoint): Float {
        return this.segmentTo(other).abs()
    }

    fun abs(): Float {
        val sumX = this.x.pow(2)
        val sumY = this.y.pow(2)

        return sqrt(sumX + sumY)
    }

    fun norm(): GeomPoint {
        return this / this.abs()
    }

    fun mirrorAt(other: GeomPoint): GeomPoint {
        return 2 * other - this
    }

    fun perpendicularCcw(): GeomPoint {
        return GeomPoint(
            this.y,
            -this.x,
        )
    }

    fun perpendicularCw(): GeomPoint {
        return GeomPoint(
            -this.y,
            this.x,
        )
    }

    fun perpendicular(cw: Boolean = false): GeomPoint {
        return if (cw) this.perpendicularCw() else this.perpendicularCcw()
    }

    private fun scalingFactors(direction: GeomPoint, boundingBox: Rectangle): List<Float> {
        val lowX = (boundingBox.startCorner.x - this.x) / direction.x
        val lowY = (boundingBox.startCorner.y - this.y) / direction.y
        val highX = (boundingBox.endCorner.x - this.x) / direction.x
        val highY = (boundingBox.endCorner.y - this.y) / direction.y

        return listOf(lowX, lowY, highX, highY).filter {
            val scaledCoord = this + it * direction
            scaledCoord in boundingBox
        }
    }

    fun maxScaling(direction: GeomPoint, boundingBox: Rectangle): Float {
        return this.scalingFactors(direction, boundingBox).max()
    }

    fun minScaling(direction: GeomPoint, boundingBox: Rectangle): Float {
        return this.scalingFactors(direction, boundingBox).min()
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
