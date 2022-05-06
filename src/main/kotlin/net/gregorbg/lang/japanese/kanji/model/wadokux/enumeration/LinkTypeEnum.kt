package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LinkTypeEnum {
    @SerialName("picture") PICTURE,
    @SerialName("audio") AUDIO,
    @SerialName("url") URL
}