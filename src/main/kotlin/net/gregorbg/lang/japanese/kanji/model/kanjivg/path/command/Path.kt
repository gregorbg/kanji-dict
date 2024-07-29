package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.CombinedPathComponent

data class Path(
    val commands: List<PathCommand<*>>,
): SvgCommand<CombinedPathComponent>, List<PathCommand<*>> by commands {
    override fun toSvg() = this.commands.joinToString(" ") { it.toSvg() }

    override fun toComponent(): CombinedPathComponent {
        val segments = this.commands.map { it.pathComponent }
        return CombinedPathComponent(segments)
    }
}
