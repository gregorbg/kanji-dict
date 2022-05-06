package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.Kanji
import net.gregorbg.lang.japanese.kanji.model.KanjiElements

interface KanjiElementsSource {
    fun getElementsFor(kanji: Kanji): KanjiElements?
}
