package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.Spline

data class Path(
    val commands: List<PathCommand<*>>,
): SvgCommand<Spline>, List<PathCommand<*>> by commands {
    override fun toSvg() = this.joinToString(" ") { it.toSvg() }

    override fun toComponent(): Spline {
        val segments = this.map { it.pathComponent }
        return Spline(segments)
    }
}
