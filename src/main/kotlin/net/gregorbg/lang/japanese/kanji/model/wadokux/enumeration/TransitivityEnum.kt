package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransitivityEnum {
    @SerialName("trans") TRANSITIVE,
    @SerialName("intrans") INTRANSITIVE,
    @SerialName("both") BOTH
}