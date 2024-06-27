package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

interface PathComponent<T : PathComponent<T>> {
    val start: GeomPoint
    val end: GeomPoint

    fun arcLength(): Float

    fun positionAt(t: Float): GeomPoint

    fun reverse(): T
}
