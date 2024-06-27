package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.CommandMode
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.PathCommand

data class CubicBezier(
    override val start: GeomPoint,
    val controlStart: GeomPoint,
    val controlEnd: GeomPoint,
    override val end: GeomPoint,
) : BezierCurve<CubicBezier>(controlStart, controlEnd) {
    override fun toSvgCommand(mode: CommandMode): PathCommand<CubicBezier> {
        val controlStartMovement = if (mode == CommandMode.RELATIVE) this.controlStart - this.start else this.controlStart
        val controlEndMovement = if (mode == CommandMode.RELATIVE) this.controlEnd - this.start else this.controlEnd
        val endMovement = if (mode == CommandMode.RELATIVE) this.end - this.start else this.end

        return PathCommand.BezierCurveCommand(mode, controlStartMovement, controlEndMovement, endMovement)
    }

    override fun positionAt(t: Float): GeomPoint {
        return (((1 - t) * (1 - t) * (1 - t)) * this.start) +
                (3 * ((1 - t) * (1 - t)) * t * this.controlStart) +
                (3 * (1 - t) * (t * t) * this.controlEnd) +
                ((t * t * t) * this.end)
    }

    override fun velocityAt(t: Float): GeomPoint {
        return ((3 * (t - 1) * (t - 1)) * this.start) +
                ((9 * (t * t) - 12 * t + 3) * this.controlStart) +
                ((-9 * (t * t) + 6 * t) * this.controlEnd) +
                ((3 * (t * t)) * this.end)
    }

    override fun reverse(): CubicBezier {
        return CubicBezier(
            this.end,
            this.controlEnd,
            this.controlStart,
            this.start,
        )
    }

    override fun extendContinuous(): CubicBezier {
        return CubicBezier(
            this.end,
            2 * this.end - this.controlEnd,
            this.controlStart + 4 * (this.end - this.controlEnd),
            this.end + (this.end - this.start) + 6 * (this.controlStart - this.controlEnd + this.end - this.controlEnd)
        )
    }
}