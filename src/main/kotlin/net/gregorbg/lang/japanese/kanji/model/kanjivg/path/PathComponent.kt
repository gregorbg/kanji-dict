package net.gregorbg.lang.japanese.kanji.model.kanjivg.path

interface PathComponent<T : PathComponent<T>> {
    val start: GeomPoint
    val end: GeomPoint

    val orderedPoints: List<GeomPoint>

    fun arcLength(): Float

    fun positionAt(t: Float): GeomPoint
    fun velocityAt(t: Float): GeomPoint

    fun reverse(): T

    fun extendContinuous(): T
    fun extendLine(): Line
}
