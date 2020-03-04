package com.suushiemaniac.lang.japanese.kanji.util

infix fun <A, B> A.toEach(items: Collection<B>) = items.map { this to it }
infix fun <A, B> Collection<A>.allTo(item: B) = map { it to item }

fun Iterable<String>.singleOrAll() = singleOrNull() ?: joinToString()
