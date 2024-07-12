package net.gregorbg.lang.japanese.kanji.model.kanjivg.path.command

import net.gregorbg.lang.japanese.kanji.model.kanjivg.path.PathComponent

interface SvgCommand<T : PathComponent<T>> {
    fun toSvg(): String
    fun toComponent(): PathComponent<T>
}