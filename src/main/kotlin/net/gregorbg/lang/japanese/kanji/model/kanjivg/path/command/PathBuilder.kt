package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*

class PathBuilder(var position: GeomPoint = ORIGIN) {
    val start = position.copy()
    val commands = mutableListOf<PathCommand<*>>()

    private fun <T : PathComponent<T>> executeCommand(
        command: Command,
        commandMode: CommandMode,
        dropControls: Int,
        createComponent: (GeomPoint) -> PathComponent<T>,
    ): PathCommand<T> {
        val backingComponent = createComponent(this.position.copy())
        val compiledCommand = PathCommand(command, commandMode, backingComponent, dropControls)

        this.commands.add(compiledCommand)
        this.position = backingComponent.end.copy()

        return compiledCommand
    }

    private fun <T : PathComponent<T>> executeCommand(
        command: Command,
        commandMode: CommandMode,
        createComponent: (GeomPoint) -> PathComponent<T>
    ) = this.executeCommand(command, commandMode, 1, createComponent)

    fun M(targetX: Float, targetY: Float): PathCommand<Line> {
        return this.executeCommand(Command.MOVE_TO, CommandMode.ABSOLUTE) {
            Line(
                it,
                GeomPoint(targetX, targetY)
            )
        }
    }

    fun m(targetDx: Float, targetDy: Float): PathCommand<Line> {
        return this.executeCommand(Command.MOVE_TO, CommandMode.RELATIVE) {
            Line(
                it,
                it + GeomPoint(targetDx, targetDy)
            )
        }
    }

    fun L(targetX: Float, targetY: Float): PathCommand<Line> {
        return this.executeCommand(Command.LINE, CommandMode.ABSOLUTE) {
            Line(
                it,
                GeomPoint(targetX, targetY)
            )
        }
    }

    fun l(targetDx: Float, targetDy: Float): PathCommand<Line> {
        return this.executeCommand(Command.LINE, CommandMode.RELATIVE) {
            Line(
                it,
                it + GeomPoint(targetDx, targetDy)
            )
        }
    }

    fun Z(): PathCommand<Line> {
        return this.executeCommand(Command.CLOSE_PATH, CommandMode.ABSOLUTE, 2) {
            Line(
                it,
                this.start.copy()
            )
        }
    }

    fun z(): PathCommand<Line> {
        return this.executeCommand(Command.CLOSE_PATH, CommandMode.RELATIVE, 2) {
            Line(
                it,
                this.start.copy()
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
    ): PathCommand<CubicBezier> {
        return this.executeCommand(Command.BEZIER_CURVE, CommandMode.ABSOLUTE) {
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
    ): PathCommand<CubicBezier> {
        return this.executeCommand(Command.BEZIER_CURVE, CommandMode.RELATIVE) {
            CubicBezier(
                it,
                it + GeomPoint(controlStartDx, controlStartDy),
                it + GeomPoint(controlEndDx, controlEndDy),
                it + GeomPoint(endDx, endDy),
            )
        }
    }

    fun S(
        controlEndX: Float,
        controlEndY: Float,
        endX: Float,
        endY: Float,
    ): PathCommand<CubicBezier> {
        return this.executeCommand(Command.SYMMETRIC_BEZIER_CURVE, CommandMode.ABSOLUTE, 2) {
            val prevComponent = this.commands.lastOrNull()?.toComponent()

            if (prevComponent is CubicBezier) {
                val lastControlPoint = prevComponent.controlEnd.copy()
                val mirroredControl = lastControlPoint.mirrorAt(it)

                CubicBezier(
                    it,
                    mirroredControl,
                    GeomPoint(controlEndX, controlEndY),
                    GeomPoint(endX, endY),
                )
            }

            CubicBezier(
                it,
                it,
                GeomPoint(controlEndX, controlEndY),
                GeomPoint(endX, endY),
            )
        }
    }

    fun s(
        controlEndDx: Float,
        controlEndDy: Float,
        endDx: Float,
        endDy: Float,
    ): PathCommand<CubicBezier> {
        return this.executeCommand(Command.SYMMETRIC_BEZIER_CURVE, CommandMode.RELATIVE, 2) {
            val prevComponent = this.commands.lastOrNull()?.toComponent()

            if (prevComponent is CubicBezier) {
                val lastControlPoint = prevComponent.controlEnd.copy()
                val mirroredControl = lastControlPoint.mirrorAt(it)

                CubicBezier(
                    it,
                    mirroredControl,
                    it + GeomPoint(controlEndDx, controlEndDy),
                    it + GeomPoint(endDx, endDy),
                )
            }

            CubicBezier(
                it,
                it,
                it + GeomPoint(controlEndDx, controlEndDy),
                it + GeomPoint(endDx, endDy),
            )
        }
    }

    operator fun invoke(command: Char, vararg coords: Float): PathCommand<*> {
        return when (command) {
            'M' -> M(coords[0], coords[1])
            'm' -> m(coords[0], coords[1])
            'L' -> L(coords[0], coords[1])
            'l' -> l(coords[0], coords[1])
            'Z' -> Z()
            'z' -> z()
            'C' -> C(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5])
            'c' -> c(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5])
            'S' -> S(coords[0], coords[1], coords[2], coords[3])
            's' -> s(coords[0], coords[1], coords[2], coords[3])
            else -> error("Unrecognized command: $command")
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
