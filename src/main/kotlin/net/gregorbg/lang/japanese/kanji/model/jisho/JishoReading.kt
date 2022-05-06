package net.gregorbg.lang.japanese.kanji.model.jisho

import kotlinx.serialization.Serializable

@Serializable
data class JishoReading(val word: String? = null, val reading: String? = null)