package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times

data class Line(
    override val start: GeomPoint,
    override val end: GeomPoint,
) : PathPrimitive<Line> {
    override fun toSvg(): String {
        return """
            M ${this.start.toSvg()}
            L ${this.end.toSvg()}
        """.trimIndent()
    }

    override fun arcLength(): Float {
        return this.start.distanceTo(this.end)
    }

    override fun positionAt(t: Float): GeomPoint {
        return this.start + t * (this.end - this.start)
    }

    override fun reverse(): Line {
        return Line(this.end, this.start)
    }
}
