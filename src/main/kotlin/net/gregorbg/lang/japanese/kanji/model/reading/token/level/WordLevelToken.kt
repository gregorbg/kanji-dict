package net.gregorbg.lang.japanese.kanji.model.reading.token.level

import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm
import net.gregorbg.lang.japanese.kanji.source.KanjiSource
import net.gregorbg.lang.japanese.kanji.util.alignSymbolsWith

interface WordLevelToken : TokenWithSurfaceForm {
    fun alignTokensBy(kanjiSource: KanjiSource): List<SymbolLevelToken> {
        return this.surfaceForm.alignSymbolsWith(this.reading, kanjiSource)
    }
}
