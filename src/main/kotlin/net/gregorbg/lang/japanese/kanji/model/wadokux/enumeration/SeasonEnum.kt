package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

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