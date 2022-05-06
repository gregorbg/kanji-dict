package net.gregorbg.lang.japanese.kanji.model.jisho

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JishoAPIData(
    val slug: String,
    @SerialName("is_common") val isCommon: Boolean = false,
    val tags: List<String>,
    val jlpt: List<String>,
    val japanese: List<JishoReading>,
    val senses: List<JishoSense>,
    val attribution: JishoAttributions
)