package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.CombinedPathComponent
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.PathPrimitive

data class Path(
    val start: GeomPoint,
    val commands: List<PathCommand<*>>,
) {
    val commandModes: List<CommandMode>
        get() = this.commands.map { it.mode }

    fun toSvg(): String {
        val moveToStart = PathCommand.MoveTo(CommandMode.ABSOLUTE, this.start)
        val allCommands = listOf(moveToStart) + this.commands

        return allCommands.joinToString("\n") { it.toSvg() }
    }

    fun toPrimitive(): CombinedPathComponent {
        val segments = compileSegments(this.start, this.commands)

        return CombinedPathComponent(
            this.start,
            segments
        )
    }

    companion object {
        private tailrec fun compileSegments(
            currentPoint: GeomPoint,
            commands: List<PathCommand<*>>,
            accu: List<PathPrimitive<*>> = emptyList()
        ): List<PathPrimitive<*>> {
            if (commands.isEmpty())
                return accu

            val nextCommand = commands.first()
            val primitive = nextCommand.toPrimitive(currentPoint)

            return compileSegments(primitive.end, commands.drop(1), accu + primitive)
        }
    }
}
