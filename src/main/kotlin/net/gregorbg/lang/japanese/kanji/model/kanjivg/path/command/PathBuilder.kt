package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint

class PathBuilder(val start: GeomPoint) {
    val commands = mutableListOf<PathCommand<*>>()

    fun M(targetX: Float, targetY: Float) {
        this.commands.add(PathCommand.MoveTo(CommandMode.ABSOLUTE, GeomPoint(targetX, targetY)))
    }

    fun m(targetDx: Float, targetDy: Float) {
        this.commands.add(PathCommand.MoveTo(CommandMode.RELATIVE, GeomPoint(targetDx, targetDy)))
    }

    fun C(
        controlStartX: Float,
        controlStartY: Float,
        controlEndX: Float,
        controlEndY: Float,
        endX: Float,
        endY: Float,
    ) {
        this.commands.add(PathCommand.BezierCurveCommand(
            CommandMode.ABSOLUTE,
            GeomPoint(controlStartX, controlStartY),
            GeomPoint(controlEndX, controlEndY),
            GeomPoint(endX, endY),
        ))
    }

    fun c(
        controlStartDx: Float,
        controlStartDy: Float,
        controlEndDx: Float,
        controlEndDy: Float,
        endDx: Float,
        endDy: Float,
    ) {
        this.commands.add(PathCommand.BezierCurveCommand(
            CommandMode.RELATIVE,
            GeomPoint(controlStartDx, controlStartDy),
            GeomPoint(controlEndDx, controlEndDy),
            GeomPoint(endDx, endDy),
        ))
    }

    fun toPath(): Path {
        return Path(this.start, this.commands)
    }
}

fun svgPath(startX: Float, startY: Float, build: PathBuilder.() -> Unit): Path {
    val start = GeomPoint(startX, startY)
    val builder = PathBuilder(start)

    builder.build()

    return builder.toPath()
}
