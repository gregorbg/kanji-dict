package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.*

class PathBuilder(var position: GeomPoint = GeomPoint.origin) {
    val start = position.copy()
    val commands = mutableListOf<PathCommand>()

    private fun executeCommand(
        command: Command,
        commandMode: CommandMode,
        dropControls: Int,
        createComponent: (GeomPoint) -> BezierCurve,
    ): PathCommand {
        val backingComponent = createComponent(this.position.copy())
        val compiledCommand = PathCommand(command, commandMode, backingComponent, dropControls)

        this.commands.add(compiledCommand)
        this.position = backingComponent.end.copy()

        return compiledCommand
    }

    private fun executeCommand(
        command: Command,
        commandMode: CommandMode,
        createComponent: (GeomPoint) -> BezierCurve
    ) = this.executeCommand(command, commandMode, 1, createComponent)

    fun imitate(imitationCommand: PathCommand): PathCommand {
        return this.executeCommand(
            imitationCommand.command,
            imitationCommand.commandMode,
            imitationCommand.dropControls,
        ) {
            val translationVector = imitationCommand.tracingCurve.start.segmentTo(it)
            imitationCommand.tracingCurve.translate(translationVector)
        }
    }

    fun M(targetX: Float, targetY: Float): PathCommand {
        return this.executeCommand(Command.MOVE_TO, CommandMode.ABSOLUTE) {
            BezierCurve(
                it,
                GeomPoint(targetX, targetY)
            )
        }
    }

    fun m(targetDx: Float, targetDy: Float): PathCommand {
        return this.executeCommand(Command.MOVE_TO, CommandMode.RELATIVE) {
            BezierCurve(
                it,
                it + GeomPoint(targetDx, targetDy)
            )
        }
    }

    fun L(targetX: Float, targetY: Float): PathCommand {
        return this.executeCommand(Command.LINE, CommandMode.ABSOLUTE) {
            BezierCurve(
                it,
                GeomPoint(targetX, targetY)
            )
        }
    }

    fun l(targetDx: Float, targetDy: Float): PathCommand {
        return this.executeCommand(Command.LINE, CommandMode.RELATIVE) {
            BezierCurve(
                it,
                it + GeomPoint(targetDx, targetDy)
            )
        }
    }

    fun Z(): PathCommand {
        return this.executeCommand(Command.CLOSE_PATH, CommandMode.ABSOLUTE, 2) {
            BezierCurve(
                it,
                this.start.copy()
            )
        }
    }

    fun z(): PathCommand {
        return this.executeCommand(Command.CLOSE_PATH, CommandMode.RELATIVE, 2) {
            BezierCurve(
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
    ): PathCommand {
        return this.executeCommand(Command.BEZIER_CURVE, CommandMode.ABSOLUTE) {
            BezierCurve(
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
    ): PathCommand {
        return this.executeCommand(Command.BEZIER_CURVE, CommandMode.RELATIVE) {
            BezierCurve(
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
    ): PathCommand {
        return this.executeCommand(Command.SYMMETRIC_BEZIER_CURVE, CommandMode.ABSOLUTE, 2) {
            val prevComponent = this.commands.lastOrNull()?.tracingCurve

            if (prevComponent is BezierCurve) {
                val lastControlPoint = prevComponent.controlPoints[prevComponent.degree].copy()
                val mirroredControl = lastControlPoint.mirrorAt(it)

                BezierCurve(
                    it,
                    mirroredControl,
                    GeomPoint(controlEndX, controlEndY),
                    GeomPoint(endX, endY),
                )
            }

            BezierCurve(
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
    ): PathCommand {
        return this.executeCommand(Command.SYMMETRIC_BEZIER_CURVE, CommandMode.RELATIVE, 2) {
            val prevComponent = this.commands.lastOrNull()?.tracingCurve

            if (prevComponent is BezierCurve) {
                val lastControlPoint = prevComponent.controlPoints[prevComponent.degree].copy()
                val mirroredControl = lastControlPoint.mirrorAt(it)

                BezierCurve(
                    it,
                    mirroredControl,
                    it + GeomPoint(controlEndDx, controlEndDy),
                    it + GeomPoint(endDx, endDy),
                )
            }

            BezierCurve(
                it,
                it,
                it + GeomPoint(controlEndDx, controlEndDy),
                it + GeomPoint(endDx, endDy),
            )
        }
    }

    operator fun invoke(command: Char, vararg coords: Float): PathCommand {
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

    fun toPath() = ParsedPath(this.commands)
}

fun svgPath(startX: Float, startY: Float, build: PathBuilder.() -> Unit): ParsedPath {
    val builder = PathBuilder()
    builder.M(startX, startY)

    builder.build()

    return builder.toPath()
}

fun svgPath(start: GeomPoint, build: PathBuilder.() -> Unit) = svgPath(start.x, start.y, build)
