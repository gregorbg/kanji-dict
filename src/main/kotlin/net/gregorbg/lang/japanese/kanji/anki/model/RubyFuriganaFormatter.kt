package net.gregorbg.lang.japanese.kanji.anki.model

import net.gregorbg.lang.japanese.kanji.model.reading.FuriganaFormatter
import net.gregorbg.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

object RubyFuriganaFormatter : FuriganaFormatter {
    override fun format(token: TokenWithSurfaceForm): String {
        if (token.reading == token.surfaceForm) {
            return token.surfaceForm
        }

        // FIXME detect when to insert gaps?
        return "${token.surfaceForm}[${token.reading}]"
    }
}