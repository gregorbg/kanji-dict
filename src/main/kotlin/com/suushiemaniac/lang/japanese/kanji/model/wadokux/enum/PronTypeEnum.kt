package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PronTypeEnum {
    @SerialName("hatsuon") PRONOUNCIATION,
    @SerialName("furigana") FURIGANA,
    @SerialName("romaji") ROMAJI
}