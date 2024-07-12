package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.CubicBezier
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.Line
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.PathComponent

class PathBuilder(var position: GeomPoint = ORIGIN) {
    val commands = mutableListOf<PathCommand<*>>()

    private fun <T : PathComponent<T>> executeCommand(
        command: Command,
        commandMode: CommandMode,
        createComponent: (GeomPoint) -> PathComponent<T>
    ) {
        val backingComponent = createComponent(this.position.copy())

        this.commands.add(PathCommand(command, commandMode, backingComponent))
        this.position = backingComponent.end.copy()
    }

    fun M(targetX: Float, targetY: Float) {
        this.executeCommand(Command.MOVE_TO, CommandMode.ABSOLUTE) {
            Line(
                it,
                GeomPoint(targetX, targetY)
            )
        }
    }

    fun m(targetDx: Float, targetDy: Float) {
        this.executeCommand(Command.MOVE_TO, CommandMode.RELATIVE) {
            Line(
                it,
                it + GeomPoint(targetDx, targetDy)
            )
        }
    }

    fun C(
        controlStartX: Float,
        controlStartY: Float,
        controlEndX: Float,
        controlEndY: Float,
        endX: Float,
        endY: Float,
    ) {
        this.executeCommand(Command.BEZIER_CURVE, CommandMode.ABSOLUTE) {
            CubicBezier(
                it,
                GeomPoint(controlStartX, controlStartY),
                GeomPoint(controlEndX, controlEndY),
                GeomPoint(endX, endY),
            )
        }
    }

    fun c(
        controlStartDx: Float,
        controlStartDy: Float,
        controlEndDx: Float,
        controlEndDy: Float,
        endDx: Float,
        endDy: Float,
    ) {
        this.executeCommand(Command.BEZIER_CURVE, CommandMode.RELATIVE) {
            CubicBezier(
                it,
                it + GeomPoint(controlStartDx, controlStartDy),
                it + GeomPoint(controlEndDx, controlEndDy),
                it + GeomPoint(endDx, endDy),
            )
        }
    }

    fun toPath(): Path {
        return Path(this.commands)
    }

    companion object {
        val ORIGIN = GeomPoint(0f, 0f)
    }
}

fun svgPath(startX: Float, startY: Float, build: PathBuilder.() -> Unit): Path {
    val builder = PathBuilder()
    builder.M(startX, startY)

    builder.build()

    return builder.toPath()
}
