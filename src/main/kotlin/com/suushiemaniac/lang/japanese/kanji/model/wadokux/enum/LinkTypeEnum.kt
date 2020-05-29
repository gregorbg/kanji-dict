package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LinkTypeEnum {
    @SerialName("picture") PICTURE,
    @SerialName("audio") AUDIO,
    @SerialName("url") URL
}