package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SeasonPartEnum {
    @SerialName("bud") BUD,
    @SerialName("flower") FLOWER,
    @SerialName("fruit") FRUIT,
    @SerialName("leaf") LEAF
}