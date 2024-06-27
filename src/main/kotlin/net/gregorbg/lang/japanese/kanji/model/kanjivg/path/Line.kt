package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.CommandMode
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.PathCommand

data class Line(
    override val start: GeomPoint,
    override val end: GeomPoint,
) : PathPrimitive<Line> {
    override fun toSvgCommand(mode: CommandMode): PathCommand<Line> {
        val movement = if (mode == CommandMode.RELATIVE) this.end - this.start else this.end
        return PathCommand.LineCommand(mode, movement)
    }

    override fun arcLength(): Float {
        return this.start.distanceTo(this.end)
    }

    override fun positionAt(t: Float): GeomPoint {
        return this.start + t * (this.end - this.start)
    }

    override fun reverse(): Line {
        return Line(this.end, this.start)
    }
}
