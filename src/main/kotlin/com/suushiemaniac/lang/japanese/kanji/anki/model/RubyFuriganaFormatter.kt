package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.reading.token.TokenWithSurfaceForm

object RubyFuriganaFormatter : FuriganaFormatter {
    override fun format(token: TokenWithSurfaceForm): String {
        if (token.reading == token.surfaceForm) {
            return token.surfaceForm
        }

        // FIXME detect when to insert gaps?
        return "${token.surfaceForm}[${token.reading}]"
    }
}