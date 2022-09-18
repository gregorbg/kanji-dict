package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.FuriganaFormatter

interface TokenWithSurfaceForm {
    val surfaceForm: String

    val reading: String

    fun asFurigana(formatter: FuriganaFormatter) =
        formatter.format(this)
}