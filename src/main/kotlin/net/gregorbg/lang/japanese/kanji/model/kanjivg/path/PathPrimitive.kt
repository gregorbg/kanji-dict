package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.CommandMode
import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command.PathCommand

interface PathPrimitive<T : PathPrimitive<T>> : PathComponent<T> {
    fun toSvgCommand(mode: CommandMode): PathCommand<T>
}
