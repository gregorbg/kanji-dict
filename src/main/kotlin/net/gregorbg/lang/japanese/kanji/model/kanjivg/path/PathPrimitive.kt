package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

interface PathPrimitive<T : PathPrimitive<T>> {
    val start: GeomPoint
    val end: GeomPoint

    fun toSvg(): String

    fun arcLength(): Float

    fun positionAt(t: Float): GeomPoint

    fun reverse(): T
}
