package com.suushiemaniac.lang.japanese.kanji.model.reading.token

data class MorphologyToken(
    override val reading: String,
    override val surfaceForm: String,
    val morphology: MorphologicalData? = null
) : TokenWithSurfaceForm
