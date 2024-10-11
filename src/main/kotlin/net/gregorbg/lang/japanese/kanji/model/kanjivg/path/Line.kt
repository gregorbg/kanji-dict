package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.GeomPoint.Companion.times

data class Line(
    override val start: GeomPoint,
    override val end: GeomPoint,
) : PathComponent<Line> {
    override val orderedPoints: List<GeomPoint>
        get() = listOf(this.start, this.end)

    override fun arcLength(): Float {
        return this.start.distanceTo(this.end)
    }

    override fun positionAt(t: Float): GeomPoint {
        return this.start + t * (this.end - this.start)
    }

    override fun velocityAt(t: Float): GeomPoint {
        return this.end - this.start
    }

    override fun positionForArc(t: Float): GeomPoint {
        return this.positionAt(t)
    }

    override fun reverse(): Line {
        return Line(this.end, this.start)
    }

    override fun extendLine(): Line {
        return Line(this.end, this.end + (this.end - this.start))
    }

    override fun translate(translation: GeomPoint): Line {
        return Line(this.start.translate(translation), this.end.translate(translation))
    }

    override fun extendContinuous(): Line {
        return this.extendLine()
    }
}
