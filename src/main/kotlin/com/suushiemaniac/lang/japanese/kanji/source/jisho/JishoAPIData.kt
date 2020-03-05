package com.suushiemaniac.lang.japanese.kanji.source.jisho

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JishoAPIData(
    val slug: String,
    @SerialName("is_common") val isCommon: Boolean,
    val tags: List<String>,
    val jlpt: List<String>,
    val japanese: List<JishoReading>,
    val senses: List<JishoSense>,
    val attributions: JishoAttributions
)