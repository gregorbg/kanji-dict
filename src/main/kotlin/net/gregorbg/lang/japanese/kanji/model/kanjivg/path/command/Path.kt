package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.CombinedPathComponent
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.PathComponent

data class Path(
    val commands: List<PathCommand<*>>,
): SvgCommand<CombinedPathComponent>, List<PathCommand<*>> by commands {
    override fun toSvg() = this.commands.joinToString("\n") { it.toSvg() }

    override fun toComponent(): PathComponent<CombinedPathComponent> {
        val segments = this.commands.map { it.pathComponent }
        return CombinedPathComponent(segments)
    }
}
