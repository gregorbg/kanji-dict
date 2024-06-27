package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.CommandMode
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.Path
import kotlin.math.abs

data class CombinedPathComponent(
    override val start: GeomPoint,
    val segments: List<PathPrimitive<*>>,
) : PathComponent<CombinedPathComponent> {
    override val end: GeomPoint
        get() = this.segments.lastOrNull()?.end ?: this.start

    override fun arcLength(): Float {
        return this.segments.map { it.arcLength() }.sum()
    }

    override fun positionAt(t: Float): GeomPoint {
        val arcLengths = this.segments.map { it.arcLength() }
        val totalArcLength = arcLengths.sum()

        val relArcLengths = arcLengths.map { it / totalArcLength }
        val runningArcSums = relArcLengths.runningReduce { a, b -> a + b }

        val lookupIndex = runningArcSums.indexOfFirst { it >= t }
        val lookupSegment = this.segments[lookupIndex]

        val untilT = relArcLengths.take(lookupIndex).sum()
        val startToT = abs(t - untilT)

        val rescaledT = startToT / relArcLengths[lookupIndex]

        return lookupSegment.positionAt(rescaledT)
    }

    override fun reverse(): CombinedPathComponent {
        return CombinedPathComponent(
            this.end,
            this.segments.map { it.reverse() }.reversed()
        )
    }

    fun toSvgModel(commandModes: List<CommandMode>): Path {
        val commands = commandModes.zip(this.segments).map { (cmd, seg) -> seg.toSvgCommand(cmd) }

        return Path(
            this.start,
            commands,
        )
    }
}
