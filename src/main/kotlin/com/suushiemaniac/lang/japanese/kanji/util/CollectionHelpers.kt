package com.suushiemaniac.lang.japanese.kanji.util

infix fun <A, B> A.toEach(items: Iterable<B>) = items.map { this to it }
infix fun <A, B> Iterable<A>.allTo(item: B) = map { it to item }

fun Iterable<String>.singleOrAll() = singleOrNull() ?: joinToString()

fun <K, V> Map<K, Iterable<V>>.invertMultiMap() =
    entries.flatMap { it.value allTo it.key }.toMap()

fun <K, V> Map<K, Collection<V>>.mergeMultiMap(other: Map<K, Collection<V>>) =
    (keys + other.keys).associateWith { this[it].orEmpty() + other[it].orEmpty() }

fun <T, V> Iterable<T>.associateWithNotNull(mapping: (T) -> V?): Map<T, V> {
    return mapNotNull { t -> mapping(t)?.let { t to it } }.toMap()
}

fun <T> Collection<T>.unlessEmpty() = takeUnless { it.isEmpty() }