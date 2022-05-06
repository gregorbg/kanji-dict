package net.gregorbg.lang.japanese.kanji.model.wadokux.enumeration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LevelEnum {
    @SerialName("1i") KAMI_ICHIDAN,
    @SerialName("1e") SHIMO_ICHIDAN,
    @SerialName("2i") KAMI_NIDAN,
    @SerialName("2e") SHIMO_NIDAN,
    @SerialName("4") YONDAN,
    @SerialName("5") GODAN,
    @SerialName("suru") SA_HEN,
    @SerialName("kuru") KA_HEN,
    @SerialName("ra") RA_HEN
}