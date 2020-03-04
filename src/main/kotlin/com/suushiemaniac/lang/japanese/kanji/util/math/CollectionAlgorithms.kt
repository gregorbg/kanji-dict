package com.suushiemaniac.lang.japanese.kanji.util.math

object CollectionAlgorithms {
    fun <T> Collection<T>.powerset(): Set<Set<T>> = powerset(this, setOf(setOf()))

    private tailrec fun <T> powerset(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> = when {
        left.isEmpty() -> acc
        else -> powerset(left.drop(1), acc + acc.map { it + left.first() })
    }

    fun <T> List<T>.permutations(): Set<List<T>> = when {
        isEmpty() -> emptySet()
        size == 1 -> setOf(listOf(this[0]))
        else -> {
            drop(1).permutations()
                    .flatMap { sublist -> (0..sublist.size).map { i -> sublist.plusAt(i, this[0]) } }
                    .toSet()
        }
    }

    internal fun <T> List<T>.plusAt(index: Int, element: T): List<T> = when (index) {
        !in 0..size -> error("Cannot put at index $index because size is $size")
        0 -> listOf(element) + this
        size -> this + element
        else -> dropLast(size - index) + element + drop(index)
    }
}