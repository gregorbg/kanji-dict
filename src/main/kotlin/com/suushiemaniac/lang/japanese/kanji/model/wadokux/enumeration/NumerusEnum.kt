package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NumerusEnum {
    @SerialName("sg") SINGULAR,
    @SerialName("pl") PLURAL
}