package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*

sealed class PathCommand<T : PathPrimitive<T>>(val mode: CommandMode, vararg val stepPoints: GeomPoint) {
    abstract val svgCommand: String

    fun toSvg(): String {
        val svgCommandTag = if (mode == CommandMode.ABSOLUTE) this.svgCommand.uppercase() else this.svgCommand.lowercase()

        return "$svgCommandTag ${stepPoints.joinToString(" ") { it.toSvg() }}"
    }

    fun toPrimitive(currentPosition: GeomPoint): T {
        val controlPoints = this.stepPoints.map {
            if (mode == CommandMode.ABSOLUTE) it else (currentPosition + it)
        }

        return this.toPrimitive(currentPosition, controlPoints)
    }

    protected abstract fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): T

    class MoveTo(mode: CommandMode, target: GeomPoint) : PathCommand<Line>(mode, target) {
        override val svgCommand = "M"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): Line {
            return Line(start, controlPoints.first())
        }
    }

    class LineCommand(mode: CommandMode, target: GeomPoint) : PathCommand<Line>(mode, target) {
        override val svgCommand = "L"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): Line {
            return Line(start, controlPoints.first())
        }
    }

    class HorizontalLine(mode: CommandMode, target: GeomPoint) : PathCommand<Line>(mode, target) {
        override val svgCommand = "H"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): Line {
            return Line(start, controlPoints.first())
        }
    }

    class VerticalLine(mode: CommandMode, target: GeomPoint) : PathCommand<Line>(mode, target) {
        override val svgCommand = "V"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): Line {
            return Line(start, controlPoints.first())
        }
    }

    class ClosePathCommand(mode: CommandMode, val globalStart: GeomPoint) : PathCommand<Line>(mode) {
        override val svgCommand = "Z"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): Line {
            return Line(start, this.globalStart)
        }
    }

    class BezierCurveCommand(mode: CommandMode, controlStart: GeomPoint, controlEnd: GeomPoint, end: GeomPoint) : PathCommand<CubicBezier>(mode, controlStart, controlEnd, end) {
        override val svgCommand = "C"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): CubicBezier {
            val (controlStart, controlEnd, end) = controlPoints

            return CubicBezier(start, controlStart, controlEnd, end)
        }
    }

    class SymmetricBezierCurve(
        mode: CommandMode,
        predecessor: BezierCurve<CubicBezier>,
        controlEnd: GeomPoint,
        end: GeomPoint,
    ) : PathCommand<CubicBezier>(mode, predecessor.controlPoints.last(), controlEnd, end) {
        override val svgCommand = "S"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): CubicBezier {
            val (controlStart, controlEnd, end) = controlPoints

            return CubicBezier(start, controlStart, controlEnd, end)
        }
    }

    class QuadraticBezierCurve(mode: CommandMode, control: GeomPoint, end: GeomPoint) : PathCommand<QuadraticBezier>(mode, control, end) {
        override val svgCommand = "Q"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): QuadraticBezier {
            val (control, end) = controlPoints

            return QuadraticBezier(start, control, end)
        }
    }

    class SymmetricQuadraticBezierCurve(
        mode: CommandMode,
        predecessor: BezierCurve<QuadraticBezier>,
        end: GeomPoint,
    ) : PathCommand<QuadraticBezier>(mode, predecessor.controlPoints.last(), end) {
        override val svgCommand = "T"

        override fun toPrimitive(start: GeomPoint, controlPoints: List<GeomPoint>): QuadraticBezier {
            val (control, end) = controlPoints

            return QuadraticBezier(start, control, end)
        }
    }
}
