package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.CommandMode
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.PathCommand

data class QuadraticBezier(
    override val start: GeomPoint,
    val controlPoint: GeomPoint,
    override val end: GeomPoint,
) : BezierCurve<QuadraticBezier>(controlPoint) {
    override fun toSvgCommand(mode: CommandMode): PathCommand<QuadraticBezier> {
        val controlMovement = if (mode == CommandMode.RELATIVE) this.controlPoint - this.start else this.controlPoint
        val endMovement = if (mode == CommandMode.RELATIVE) this.end - this.start else this.end

        return PathCommand.QuadraticBezierCurve(mode, controlMovement, endMovement)
    }

    override fun positionAt(t: Float): GeomPoint {
        return (((1 - t) * (1 - t)) * this.start) +
                (2 * t * (1 - t) * this.controlPoint) +
                ((t * t) * this.end)
    }

    override fun velocityAt(t: Float): GeomPoint {
        return (2 * (t - 1) * this.start) +
                ((2 - 4 * t) * this.controlPoint) +
                ((2 * t) * this.end)
    }

    override fun extendContinuous(): QuadraticBezier {
        return QuadraticBezier(
            this.end,
            2 * this.end - this.controlPoint,
            this.start - 2 * this.controlPoint + 3 * this.end
        )
    }

    override fun reverse(): QuadraticBezier {
        return QuadraticBezier(
            this.end,
            this.controlPoint,
            this.start,
        )
    }
}
