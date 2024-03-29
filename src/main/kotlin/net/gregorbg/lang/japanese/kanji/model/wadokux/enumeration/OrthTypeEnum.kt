package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OrthTypeEnum {
    @SerialName("irreg") IRREGULAR,
    @SerialName("read") READING,
    @SerialName("long") LONG,
    @SerialName("mistake") MISTAKE
}