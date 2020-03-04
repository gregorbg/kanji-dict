package com.suushiemaniac.lang.japanese.kanji.anki.model

import com.suushiemaniac.lang.japanese.kanji.model.reading.FuriganaFormatter
import com.suushiemaniac.lang.japanese.kanji.model.reading.ReadingWithSurfaceForm

object RubyFuriganaFormatter : FuriganaFormatter {
    override fun format(reading: ReadingWithSurfaceForm): String {
        if (reading.reading == reading.surfaceForm) {
            return reading.surfaceForm
        }

        // FIXME detect when to insert gaps?
        return "${reading.surfaceForm}[${reading.reading}]"
    }
}