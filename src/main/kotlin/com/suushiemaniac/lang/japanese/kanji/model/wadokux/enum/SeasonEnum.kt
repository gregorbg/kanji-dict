package com.suushiemaniac.lang.japanese.kanji.model.wadokux.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SeasonEnum {
    @SerialName("summer") SUMMER,
    @SerialName("autumn") AUTUMN,
    @SerialName("spring") SPRING,
    @SerialName("winter") WINTER,
    @SerialName("newyear") NEWYEAR
}