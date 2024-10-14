package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.BezierCurve

data class ParsedPath(
    val commands: List<PathCommand>,
): List<PathCommand> by commands {
    fun toSvg() = this.joinToString(" ") { it.toSvg() }

    fun trace(): List<BezierCurve> {
        return commands.drop(1).map { it.tracingCurve }
    }
}
