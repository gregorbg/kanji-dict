package com.suushiemaniac.lang.japanese.kanji.model.reading

interface CompositeReading : ReadingWithSurfaceForm {
    val readingParts: List<ReadingWithSurfaceForm>

    override val reading: String
        get() = readingParts.joinToString("") { it.reading }

    override val surfaceForm: String
        get() = readingParts.joinToString("") { it.surfaceForm }

    override fun asFurigana(formatter: FuriganaFormatter) =
        readingParts.joinToString("") { it.asFurigana(formatter) }
}
