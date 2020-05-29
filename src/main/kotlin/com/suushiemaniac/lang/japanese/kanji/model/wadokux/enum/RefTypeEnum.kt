package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RefTypeEnum {
    @SerialName("main") MAIN,
    @SerialName("syn") SYNONYM,
    @SerialName("anto") ANTONYM,
    @SerialName("altread") ALTERNATIVE_READING,
    @SerialName("alttranscr") ALTERNATIVE_TRANSCRIPTION,
    @SerialName("other") OTHER
}