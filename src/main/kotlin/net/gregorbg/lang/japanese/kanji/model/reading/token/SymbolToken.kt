package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.FuriganaFormatter
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.util.ConvertedSymbolTokens
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith

interface SymbolToken : TokenWithSurfaceForm {
    fun asFurigana(formatter: FuriganaFormatter) =
        formatter.format(this)

    fun alignBy(kanjiSource: KanjiSource): SymbolToken {
        val aligned = this.surfaceForm.alignSymbolsWith(this.reading, kanjiSource)
        return ConvertedSymbolTokens(aligned)
    }

    override fun asSymbols() = this
}