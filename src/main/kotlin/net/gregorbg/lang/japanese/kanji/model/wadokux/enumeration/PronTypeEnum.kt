package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PronTypeEnum {
    @SerialName("hatsuon") PRONOUNCIATION,
    @SerialName("furigana") FURIGANA,
    @SerialName("romaji") ROMAJI
}