package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.compose.CompositeSymbolTokens
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.util.ConvertedSymbolTokens
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith

data class MorphologyToken(
    override val surfaceForm: String,
    override val reading: String,
    val morphology: MorphologicalData? = null
) : WordLevelToken {
    override fun toAlignedSymbols(kanjiSource: KanjiSource): CompositeSymbolTokens<AlignedSymbolToken> {
        val aligned = this.surfaceForm.alignSymbolsWith(this.reading, kanjiSource)
        return ConvertedSymbolTokens(aligned)
    }

    override fun toSymbols(): CompositeSymbolTokens<SymbolToken> {
        val aligned = this.surfaceForm.alignSymbolsWith(this.reading)
        return ConvertedSymbolTokens(aligned)
    }
}
