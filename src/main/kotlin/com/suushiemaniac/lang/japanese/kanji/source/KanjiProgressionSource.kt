package com.suushiemaniac.lang.japanese.kanji.source

import com.suushiemaniac.lang.japanese.kanji.model.Kanji

interface KanjiProgressionSource : KanjiSource, Comparator<Kanji> {
    fun fetchAll(): List<Kanji>

    fun getOrderingIndexFor(kanji: Kanji): Int

    override fun compare(o1: Kanji, o2: Kanji): Int {
        return getOrderingIndexFor(o1).compareTo(getOrderingIndexFor(o2))
    }
}
