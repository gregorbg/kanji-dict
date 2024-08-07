package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*

data class PathCommand<T : PathComponent<T>>(
    val command: Command,
    val commandMode: CommandMode,
    val pathComponent: PathComponent<T>,
    val dropControls: Int = 1,
): SvgCommand<T> {
    override fun toSvg(): String {
        val svgCommandTag = if (commandMode == CommandMode.ABSOLUTE) this.command.svgCommand.uppercase() else this.command.svgCommand.lowercase()
        val controlsExceptFirst = pathComponent.orderedPoints.drop(this.dropControls)

        return "$svgCommandTag ${controlsExceptFirst.joinToString(" ") { it.toSvg() }}"
    }

    override fun toComponent() = pathComponent
}
