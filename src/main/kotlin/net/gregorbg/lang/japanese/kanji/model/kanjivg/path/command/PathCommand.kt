package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*

data class PathCommand<T : PathComponent<T>>(
    val command: Command,
    val commandMode: CommandMode,
    val pathComponent: PathComponent<T>,
    val dropControls: Int = 1,
): SvgCommand<T> {
    override fun toSvg(): String {
        val controlsExceptFirst = pathComponent.orderedPoints.drop(this.dropControls)

        val svgCommandTag = if (commandMode == CommandMode.ABSOLUTE) this.command.svgCommand.uppercase() else this.command.svgCommand.lowercase()
        val svgCommandCoords = if (commandMode == CommandMode.ABSOLUTE) controlsExceptFirst else controlsExceptFirst.map { this.pathComponent.start.segmentTo(it) }

        return "$svgCommandTag ${svgCommandCoords.joinToString(" ") { it.toSvg() }}"
    }

    override fun toComponent() = pathComponent
}
