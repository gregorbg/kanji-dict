package net.gregorbg.lang.japanese.kanji.model

import net.gregorbg.lang.japanese.kanji.source.KanjiElementsSource
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.util.associateWithNotNull
import net.gregorbg.lang.japanese.kanji.util.mapValuesNotNull

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