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
    ) {
        val backingComponent = createComponent(this.position.copy())

        this.commands.add(PathCommand(command, commandMode, backingComponent, dropControls))
        this.position = backingComponent.end.copy()
    }

    private fun <T : PathComponent<T>> executeCommand(
        command: Command,
        commandMode: CommandMode,
        createComponent: (GeomPoint) -> PathComponent<T>
    ) = this.executeCommand(command, commandMode, 1, createComponent)

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

    fun L(targetX: Float, targetY: Float) {
        this.executeCommand(Command.LINE, CommandMode.ABSOLUTE) {
            Line(
                it,
                GeomPoint(targetX, targetY)
            )
        }
    }

    fun l(targetDx: Float, targetDy: Float) {
        this.executeCommand(Command.LINE, CommandMode.RELATIVE) {
            Line(
                it,
                it + GeomPoint(targetDx, targetDy)
            )
        }
    }

    fun Z() {
        this.executeCommand(Command.CLOSE_PATH, CommandMode.ABSOLUTE, 2) {
            Line(
                it,
                this.start.copy()
            )
        }
    }

    fun z() {
        this.executeCommand(Command.CLOSE_PATH, CommandMode.RELATIVE, 2) {
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

    fun S(
        controlEndX: Float,
        controlEndY: Float,
        endX: Float,
        endY: Float,
    ) {
        this.executeCommand(Command.SYMMETRIC_BEZIER_CURVE, CommandMode.ABSOLUTE, 2) {
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
    ) {
        this.executeCommand(Command.SYMMETRIC_BEZIER_CURVE, CommandMode.RELATIVE, 2) {
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
