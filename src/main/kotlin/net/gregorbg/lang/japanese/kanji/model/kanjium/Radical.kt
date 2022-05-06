package net.gregorbg.lang.japanese.kanji.model.kanjium

import kotlinx.serialization.Serializable

@Serializable
data class Radical(
    val radical: Char,
    val parentRadical: Radical?,
    val number: Int,
    val strokes: Int,
    val names: List<String>,
    val meanings: List<String>,
    val notes: String?
)