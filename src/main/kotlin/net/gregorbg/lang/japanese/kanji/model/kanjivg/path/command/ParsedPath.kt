package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.BezierCurve
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint

data class ParsedPath(
    val commands: List<PathCommand>,
): List<PathCommand> by commands {
    fun toSvg() = this.joinToString(" ") { it.toSvg() }

    fun trace(): List<BezierCurve> {
        return this.commands.drop(1).map { it.tracingCurve }
    }

    fun endsWithHook(): Boolean {
        val lastBezier = this.trace().last()
        val controlAngle = lastBezier.outerControlAngle()

        return GeomPoint.radiansToDegrees(controlAngle) < 90
    }
}
