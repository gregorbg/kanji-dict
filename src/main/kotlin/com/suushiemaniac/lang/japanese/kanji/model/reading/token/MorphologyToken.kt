package com.suushiemaniac.lang.japanese.kanji.model.reading.token

data class MorphologyToken(
    override val surfaceForm: String,
    override val reading: String,
    val morphology: MorphologicalData? = null
) : TokenWithSurfaceForm
