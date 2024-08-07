package net.gregorbg.lang.japanese.kanji.util

infix fun <A, B> A.toEach(items: Iterable<B>) = items.map { this to it }
infix fun <A, B> Iterable<A>.allTo(item: B) = map { it to item }

fun Iterable<String>.singleOrAll() = singleOrNull() ?: joinToString()

fun List<String>.trimEmptyChars() = dropWhile { it.isEmpty() }.dropLastWhile { it.isEmpty() }

fun <K, V> Map<K, V>.invert() = entries.associate { it.value to it.key }

fun <K, V> Map<K, Iterable<V>>.invertMultiMap() =
    entries.flatMap { it.value allTo it.key }.toMap()

fun <K, V> Map<K, Collection<V>>.mergeMultiMap(other: Map<K, Collection<V>>) =
    (keys + other.keys).associateWith { this[it].orEmpty() + other[it].orEmpty() }

fun <K, V> Map<K, Collection<V>>.mergeToMultiMap(other: Map<K, V>) =
    (keys + other.keys).associateWith {
        other[it]?.let { v -> this[it].orEmpty() + v } ?: this[it].orEmpty()
    }

fun <K, V> Collection<Map<K, V>>.reduceToMultiMap() =
    fold(mapOf<K, Collection<V>>()) { acc, map -> acc.mergeToMultiMap(map) }

fun <T, V> Iterable<T>.associateWithNotNull(mapping: (T) -> V?): Map<T, V> {
    return mapNotNull { t -> mapping(t)?.let { t to it } }.toMap()
}

fun <K, V1, V2> Map<K, V1>.mapValuesNotNull(mapping: (Map.Entry<K, V1>) -> V2?): Map<K, V2> {
    return mapNotNull { t -> mapping(t)?.let { t.key to it } }.toMap()
}

fun <T> Collection<T>.unlessEmpty() = takeUnless { it.isEmpty() }
fun <T> List<T>.unlessEmpty() = takeUnless { it.isEmpty() }

fun <T> T.singletonList() = listOf(this)
fun <T> T.repeatList(size: Int) = List(size) { this }

fun <T> Iterable<T>.interlace(glue: T) =
    this.flatMap { listOf(it, glue) }
fun <T> List<Iterable<T>>.interlace(glue: T) =
    if (this.isEmpty()) emptyList() else this.reduce { acc, iter -> acc + glue + iter }

fun <S, T> Iterable<T>.decompose(delimiter: S, mapping: (T) -> S) = decomposeRecursive(delimiter, mapping, emptyList())

fun <T> Iterable<T>.decompose(delimiter: T) = decompose(delimiter) { it }

private tailrec fun <S, T> Iterable<T>.decomposeRecursive(
    delimiter: S,
    mapping: (T) -> S,
    accu: List<List<T>>
): List<List<T>> {
    if (this.none()) {
        return accu
    }

    val nextChunk = this.takeWhile { mapping(it) != delimiter }
    val remaining = this.drop(nextChunk.size + 1) // +1 for the delimiter itself

    val newAccu = accu.toMutableList().apply { add(nextChunk) }

    return remaining.decomposeRecursive(delimiter, mapping, newAccu)
}

fun <E> Collection<Collection<E>>.transpose(): List<List<E>> {
    fun <E> E.append(xs: List<E>): List<E> = listOf(this@append).plus(xs)

    return filter { it.isNotEmpty() }.unlessEmpty()?.let { ys ->
        ys.map { it.first() }.append(ys.map { it.drop(1) }.transpose())
    } ?: emptyList()
}

fun <E> List<E>.cycle(steps: Int): List<E> {
    val cycleStep = steps % this.size
    return (this + this).drop(cycleStep).dropLast(this.size - cycleStep)
}