package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SeasonPartEnum {
    @SerialName("bud") BUD,
    @SerialName("flower") FLOWER,
    @SerialName("fruit") FRUIT,
    @SerialName("leaf") LEAF
}