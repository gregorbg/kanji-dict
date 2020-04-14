package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.Kanji
import com.suushiemaniac.lang.japanese.kanji.model.KanjiElements

interface KanjiElementsSource {
    fun getElementsFor(kanji: Kanji): KanjiElements?
}
