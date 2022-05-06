package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.source.KanjiSource

interface AlignedSymbolToken : SymbolToken {
    val normalizedReading: String
        get() = this.reading

    override fun alignBy(kanjiSource: KanjiSource) = this
}
