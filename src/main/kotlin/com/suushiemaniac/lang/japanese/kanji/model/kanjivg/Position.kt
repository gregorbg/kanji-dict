package com.suushiemaniac.lang.japanese.kanji.model.kanjivg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Position {
    @SerialName("left") LEFT,
    @SerialName("right") RIGHT,
    @SerialName("top") TOP,
    @SerialName("bottom") BOTTOM,
    @SerialName("nyo") NYO,
    @SerialName("tare") TARE,
    @SerialName("kamae") KAMAE,
    @SerialName("kamae1") KAMAE1,
    @SerialName("kamae2") KAMAE2
}