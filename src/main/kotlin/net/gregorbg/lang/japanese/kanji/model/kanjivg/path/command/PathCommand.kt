package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*

data class PathCommand(
    val command: Command,
    val commandMode: CommandMode,
    val tracingCurve: BezierCurve,
    val dropControls: Int = 1,
) {
    fun toSvg(): String {
        val controlsExceptFirst = tracingCurve.orderedPoints.drop(this.dropControls)

        val svgCommandTag = if (commandMode == CommandMode.ABSOLUTE) this.command.svgCommand.uppercase() else this.command.svgCommand.lowercase()
        val svgCommandCoords = if (commandMode == CommandMode.ABSOLUTE) controlsExceptFirst else controlsExceptFirst.map { this.tracingCurve.start.segmentTo(it) }

        return "$svgCommandTag ${svgCommandCoords.joinToString(" ") { it.toSvg() }}"
    }
}
