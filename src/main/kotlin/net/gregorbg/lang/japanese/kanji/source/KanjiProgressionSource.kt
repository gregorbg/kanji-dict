package net.gregorbg.lang.japanese.kanji.source

import net.gregorbg.lang.japanese.kanji.model.Kanji

interface KanjiProgressionSource : KanjiSource, Comparator<Kanji> {
    fun fetchAll(): List<Kanji>

    fun lookupIndex(index: Int): Kanji?

    fun getOrderingIndexFor(kanji: Kanji): Int

    override fun compare(o1: Kanji, o2: Kanji): Int {
        return getOrderingIndexFor(o1).compareTo(getOrderingIndexFor(o2))
    }
}
