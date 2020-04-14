package com.suushiemaniac.lang.japanese.kanji.model

import com.suushiemaniac.lang.japanese.kanji.source.KanjiElementsSource
import com.suushiemaniac.lang.japanese.kanji.source.KanjiSource
import com.suushiemaniac.lang.japanese.kanji.util.associateWithNotNull
import com.suushiemaniac.lang.japanese.kanji.util.mapValuesNotNull

interface KanjiElements {
    val components: Set<Char>

    val idc: String

    companion object {
        fun KanjiElements.cleanComponents(
            symbolSource: KanjiSource,
            elementsSource: KanjiElementsSource
        ): Set<Char> {
            val kanjis = components.associateWithNotNull { symbolSource.lookupSymbol(it) }
            val subElements = kanjis.mapValuesNotNull { elementsSource.getElementsFor(it.value) }

            return components.filter { sym ->
                val remaining = (subElements - sym).values - this
                val cleanRemaining = remaining.flatMap { it.cleanComponents(symbolSource, elementsSource) }

                sym !in cleanRemaining
            }.toSet()
        }
    }
}