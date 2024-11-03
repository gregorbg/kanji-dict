package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.round
import kotlin.math.sin

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
        return this.segmentTo(other).norm()
    }

    fun norm(): Float {
        return hypot(this.x, this.y)
    }

    fun unit(): GeomPoint {
        return this / this.norm()
    }

    fun mirrorAt(other: GeomPoint): GeomPoint {
        return 2 * other - this
    }

    fun angleToXAxis(): Float {
        return abs((atan2(
            this.y,
            this.x,
        ) + 2 * PI) % (2 * PI)).toFloat()
    }

    fun angleTo(other: GeomPoint): Float {
        return acos(this.dotProduct(other) / (this.norm() * other.norm()))
    }

    fun translate(translation: GeomPoint): GeomPoint {
        return this + translation
    }

    fun rotate(angleRadians: Float): GeomPoint {
        return GeomPoint(
            this.x * cos(angleRadians) - this.y * sin(angleRadians),
            this.x * sin(angleRadians) + this.y * cos(angleRadians),
        )
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

    fun dotProduct(other: GeomPoint): Float {
        return this.x * other.x + this.y * other.y
    }

    fun crossProduct(other: GeomPoint): Float {
        return this.x * other.y - this.y * other.x
    }

    fun toSvg(): String = "${toSvgNumber(this.x)},${toSvgNumber(this.y)}"

    companion object {
        val origin: GeomPoint
            get() = GeomPoint(0f, 0f)

        val unit: GeomPoint
            get() = GeomPoint(1f, 1f)

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

        operator fun Long.times(point: GeomPoint): GeomPoint {
            return GeomPoint(
                this * point.x,
                this * point.y,
            )
        }

        fun grahamScan(points: List<GeomPoint>): List<GeomPoint> {
            val anchor = points.minWith(compareBy({ it.y }, { it.x }))

            val sortedPoints = points.sortedWith(compareBy<GeomPoint> {
                anchor.segmentTo(it).angleToXAxis()
            }.thenBy { anchor.segmentTo(it).norm() })

            return sortedPoints.fold(listOf(anchor)) { hull, point ->
                removeClockwiseTurns(hull, point) + point
            }
        }

        private tailrec fun removeClockwiseTurns(hull: List<GeomPoint>, nextPoint: GeomPoint): List<GeomPoint> {
            if (hull.size < 2) {
                return hull
            }

            val (a, b) = hull.takeLast(2)
            val crossProduct = a.segmentTo(b).crossProduct(a.segmentTo(nextPoint))

            if (crossProduct > 0) {
                return hull
            }

            return removeClockwiseTurns(hull.dropLast(1), nextPoint)
        }

        fun toSvgNumber(value: Float): String {
            val rounded = round(value * 100) / 100
            return rounded.toString().dropLastWhile { it == '0' }.trimEnd('.')
        }

        fun radiansToDegrees(rad: Float): Float {
            return rad * 180f / PI.toFloat()
        }
    }
}
