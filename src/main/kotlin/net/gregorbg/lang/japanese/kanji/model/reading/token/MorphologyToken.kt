package net.gregorbg.lang.japanese.kanji.model.reading.token

import net.gregorbg.lang.japanese.kanji.model.reading.token.level.WordLevelToken

data class MorphologyToken(
    override val surfaceForm: String,
    override val reading: String,
    val morphology: MorphologicalData? = null
) : WordLevelToken
